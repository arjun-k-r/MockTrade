/*
 * Author: Balch
 * Created: 9/4/14 12:26 AM
 *
 * This file is part of MockTrade.
 *
 * MockTrade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MockTrade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MockTrade.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2014
 */

package com.balch.mocktrade.account.strategies;

import android.util.Log;

import com.balch.mocktrade.account.Account;
import com.balch.mocktrade.finance.Quote;
import com.balch.mocktrade.investment.Investment;
import com.balch.mocktrade.order.Order;
import com.balch.mocktrade.portfolio.PortfolioUpdateBroadcaster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;

public class DogsOfTheDow extends BaseStrategy {
    private static final String TAG = DogsOfTheDow.class.getSimpleName();

    private static final String[] DOW_SYMBOLS=
            {"AXP","BA","CAT","CSCO","CVX","DD","XOM","GE","GS","HD",
            "IBM","INTC","JNJ","KO","JPM","MCD","MMM","MRK","MSFT","NKE",
            "PFE","PG","T","TRV","UNH","UTX","VZ","V","WMT","DIS"};

    public void initialize(final Account account) {
        financeModel.getQuotes(Arrays.asList(DOW_SYMBOLS))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(quoteMap -> handleQuotes(account, quoteMap),
                        throwable -> Log.e(TAG, "Dogs of the Dow getQuotes error", throwable));
    }

    @Override
    public void dailyUpdate(Account account, List<Investment> investments,
                            Map<String, Quote> quoteMap) {
        if ((investments != null) && (investments.size() > 0)) {
            Calendar createDate = new GregorianCalendar();
            createDate.setTime(investments.get(0).getCreateTime());

            Calendar now = new GregorianCalendar();
            if (createDate.get(Calendar.YEAR) < now.get(Calendar.YEAR)) {
                Account updatedAccount = sellAll(account, investments, quoteMap);
                initialize(updatedAccount);
            }
        }
    }

    private Account sellAll(Account account, List<Investment> investments,
                           Map<String, Quote> quoteMap) {
        for (Investment i : investments) {
            Quote quote = quoteMap.get(i.getSymbol());
            Order order = new Order();
            order.setAccount(account);
            order.setSymbol(quote.getSymbol());
            order.setAction(Order.OrderAction.SELL);
            order.setStrategy(Order.OrderStrategy.MANUAL);
            order.setLimitPrice(quote.getPrice());
            order.setQuantity(i.getQuantity());

            portfolioModel.createOrder(order);
            try {
                portfolioModel.attemptExecuteOrder(order, quote);
            } catch (Exception e) {
                Log.e(TAG, "Error executing order", e);
            }
        }

        // requery the account to get the latest values
        return portfolioModel.getAccount(account.getId());
    }


    private void handleQuotes(Account account, Map<String, Quote> quoteMap) {
        List<Quote> sortedQuotes = new ArrayList<>(quoteMap.values());
        Collections.sort(sortedQuotes, (lhs, rhs) -> {
            // reverse sort
            return rhs.getDividendPerShare().compareTo(lhs.getDividendPerShare());
        });

        if (sortedQuotes.size() > 0) {
            int size = sortedQuotes.size();
            int numberOfStocks = Math.min(size, 10);
            double fundsPerOrder = account.getAvailableFunds().getDollars()
                    / (double) numberOfStocks;

            for (int x = 0; x < numberOfStocks; x++) {
                Quote quote = sortedQuotes.get(x);
                Order order = new Order();
                order.setAccount(account);
                order.setSymbol(quote.getSymbol());
                order.setAction(Order.OrderAction.BUY);
                order.setStrategy(Order.OrderStrategy.MANUAL);
                order.setLimitPrice(quote.getPrice());
                order.setQuantity((long) (fundsPerOrder / quote.getPrice().getDollars()));

                portfolioModel.createOrder(order);
                try {
                    portfolioModel.attemptExecuteOrder(order, quote);
                } catch (Exception e) {
                    Log.e(TAG, "Error executing order", e);
                }
            }

            PortfolioUpdateBroadcaster.broadcast(context);
        }
    }
}

