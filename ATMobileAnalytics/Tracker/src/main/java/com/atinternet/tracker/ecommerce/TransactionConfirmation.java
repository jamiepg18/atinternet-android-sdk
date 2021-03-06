/*
 * This SDK is licensed under the MIT license (MIT)
 * Copyright (c) 2015- Applied Technologies Internet SAS (registration number B 403 261 258 - Trade and Companies Register of Bordeaux – France)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.atinternet.tracker.ecommerce;

import com.atinternet.tracker.Event;
import com.atinternet.tracker.Screen;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.TrackerConfigurationKeys;
import com.atinternet.tracker.Utility;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceCart;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceCustomer;
import com.atinternet.tracker.ecommerce.objectproperties.ECommercePayment;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceProduct;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceShipping;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionConfirmation extends Event {

    private Tracker tracker;
    private Screen screen;

    private ECommerceCart cart;
    private List<String> promotionalCodes;
    private ECommerceTransaction transaction;
    private ECommerceShipping shipping;
    private ECommercePayment payment;
    private ECommerceCustomer customer;
    private List<ECommerceProduct> products;


    TransactionConfirmation(Tracker tracker, Screen screen) {
        super("transaction.confirmation");
        this.tracker = tracker;
        this.screen = screen;
        cart = new ECommerceCart();
        promotionalCodes = new ArrayList<>();
        transaction = new ECommerceTransaction();
        shipping = new ECommerceShipping();
        payment = new ECommercePayment();
        customer = new ECommerceCustomer();
        products = new ArrayList<>();
    }

    public ECommerceCart Cart() {
        return cart;
    }

    public List<String> PromotionalCodes() {
        return promotionalCodes;
    }

    public ECommerceTransaction Transaction() {
        return transaction;
    }

    public ECommerceShipping Shipping() {
        return shipping;
    }

    public ECommercePayment Payment() {
        return payment;
    }

    public ECommerceCustomer Customer() {
        return customer;
    }

    public List<ECommerceProduct> Products() {
        return products;
    }

    @Override
    protected Map<String, Object> getData() {
        data.put("cart", cart.getAll());
        data.put("payment", payment.getAll());
        data.put("customer", customer.getAll());
        data.put("shipping", shipping.getAll());
        data.put("transaction", transaction.getAll());
        data.put("a:s:promotionalCode", promotionalCodes);
        return super.getData();
    }

    @Override
    protected List<Event> getAdditionalEvents() {
        /// SALES INSIGHTS
        List<Event> generatedEvents = super.getAdditionalEvents();

        CartConfirmation cc = new CartConfirmation();
        cc.Transaction().setAll(transaction.getAll());
        cc.Cart().setAll(cart.getAll());
        generatedEvents.add(cc);

        for (ECommerceProduct p : products) {
            ProductPurchased pp = new ProductPurchased();
            pp.Cart().set("id", String.valueOf(cart.get("s:id")));
            pp.Transaction().set("id", String.valueOf(transaction.get("s:id")));
            pp.Product().setAll(p.getAll());
            generatedEvents.add(pp);
        }

        /// SALES TRACKER
        if (screen != null && Utility.parseBooleanFromString(String.valueOf(tracker.getConfiguration().get(TrackerConfigurationKeys.AUTO_SALES_TRACKER)))) {
            double turnoverTaxIncluded = Utility.parseDoubleFromString(String.valueOf(cart.get("f:turnoverTaxIncluded")));
            double turnoverTaxFree = Utility.parseDoubleFromString(String.valueOf(cart.get("f:turnoverTaxFree")));

            String[] codes = new String[promotionalCodes.size()];
            promotionalCodes.toArray(codes);
            tracker.Orders().add(String.valueOf(transaction.get("s:id")), Utility.parseDoubleFromString(String.valueOf(cart.get("f:turnoverTaxIncluded"))))
                    .setStatus(3).setPaymentMethod(0).setConfirmationRequired(false).setNewCustomer(Utility.parseBooleanFromString(String.valueOf(customer.get("b:new"))))
                    .Delivery().set(Utility.parseDoubleFromString(String.valueOf(shipping.get("f:costTaxFree"))), Utility.parseDoubleFromString(String.valueOf(shipping.get("f:costTaxIncluded"))), String.valueOf(shipping.get("s:delivery")))
                    .Amount().set(turnoverTaxFree, turnoverTaxIncluded, turnoverTaxIncluded - turnoverTaxFree)
                    .Discount().setPromotionalCode(Utility.stringJoin('|', codes));

            com.atinternet.tracker.Cart stCart = tracker.Cart().set(String.valueOf(cart.get("s:id")));
            for (ECommerceProduct p : products) {
                String stProductId;
                Object name = p.get("s:name");
                if (name != null) {
                    stProductId = String.format("%s[%s]", String.valueOf(p.get("s:id")), String.valueOf(name));
                } else {
                    stProductId = String.valueOf(p.get("s:id"));
                }

                com.atinternet.tracker.Product stProduct = stCart.Products().add(stProductId)
                        .setQuantity(Utility.parseIntFromString(String.valueOf(p.get("n:quantity"))))
                        .setUnitPriceTaxIncluded(Utility.parseDoubleFromString(String.valueOf(p.get("f:priceTaxIncluded"))))
                        .setUnitPriceTaxFree(Utility.parseDoubleFromString(String.valueOf(p.get("f:priceTaxFree"))));

                Object stCategory = p.get("s:category1");
                if (stCategory != null) {
                    stProduct.setCategory1(String.format("[%s]", String.valueOf(stCategory)));
                }
                stCategory = p.get("s:category2");
                if (stCategory != null) {
                    stProduct.setCategory2(String.format("[%s]", String.valueOf(stCategory)));
                }
                stCategory = p.get("s:category3");
                if (stCategory != null) {
                    stProduct.setCategory3(String.format("[%s]", String.valueOf(stCategory)));
                }
                stCategory = p.get("s:category4");
                if (stCategory != null) {
                    stProduct.setCategory4(String.format("[%s]", String.valueOf(stCategory)));
                }
                stCategory = p.get("s:category5");
                if (stCategory != null) {
                    stProduct.setCategory5(String.format("[%s]", String.valueOf(stCategory)));
                }
                stCategory = p.get("s:category6");
                if (stCategory != null) {
                    stProduct.setCategory6(String.format("[%s]", String.valueOf(stCategory)));
                }

            }
            screen.setTimestamp(System.nanoTime());
            screen.setCart(stCart);
            screen.setIsBasketScreen(false).sendView();
            screen.setCart(null);
            stCart.unset();
        }

        return generatedEvents;
    }
}
