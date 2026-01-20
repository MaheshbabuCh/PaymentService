package com.lld.payment_service.paymentgateways.stripepaymentgateway;

import com.lld.payment_service.paymentgateways.PaymentGateway;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class StripePaymentGateway implements PaymentGateway {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public String generatePaymentLink(String orderId, long amount, String email) {
        // Set your secret key. Remember to switch to your live secret key in production.
        // See your keys here: https://dashboard.stripe.com/apikeys

        // Logic to generate payment link
        Stripe.apiKey = stripeApiKey;

        ProductCreateParams productCreateParams =
                ProductCreateParams.builder().setName("Samsung Washing Machine").build();

        Product product = null;
        try {
            product = Product.create(productCreateParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
        PriceCreateParams priceCreateParams =
                PriceCreateParams.builder()
                        .setCurrency("inr")
                        .setUnitAmount(amount)
                        .setProduct(product.getId())
                        .build();
       Price price;
        try {
            price = Price.create(priceCreateParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        PaymentLinkCreateParams params =
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(price.getId())
                                        .setQuantity(1L)
                                        .build()
                        )
                        .build();

        PaymentLink paymentLink;

        try {
             paymentLink = PaymentLink.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
        return paymentLink.getUrl();
    }
}
