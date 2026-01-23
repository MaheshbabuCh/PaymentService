// java
package com.lld.payment_service.webhooks;

import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeObject;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks/stripe")
public class StripeWebhookController {

    //@Value("${stripe.webhook.secret}")
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping(value = "/confirm-order", consumes = "application/json")
    public ResponseEntity<String> orderConfirmed(
            @RequestBody String payload,
            @RequestHeader(value = "Stripe-Signature", required = false) String sigHeader) {

        if (payload == null || payload.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty payload");
        }

        Event event;
        try {
            if (endpointSecret != null && !endpointSecret.isBlank() && sigHeader != null) {
                event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            } else {
                // Fallback to parsing without signature verification (not recommended for prod)
                event = ApiResource.GSON.fromJson(payload, Event.class);
            }
        } catch (SignatureVerificationException e) {
            System.out.println("Webhook signature verification failed.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (JsonSyntaxException e) {
            System.out.println(" Webhook payload JSON parsing failed.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        } catch (Exception e) {
            System.out.println("Unexpected error parsing webhook: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        }

        if (event == null || event.getType() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid event");
        }

        // Deserialize nested object
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed (API version mismatch etc.) — handle as needed
        }

        // java
        switch (event.getType()) {

            // handle created PaymentIntent:
            case "payment_intent.created":
                if (event.getData().getObject() instanceof PaymentIntent) {
                    PaymentIntent createdIntent = (PaymentIntent) event.getData().getObject();
                    System.out.println("Payment intent created: " + createdIntent.getId());
                    System.out.println(event.toJson());
                } else {
                    System.out.println("payment_intent.created but object is not PaymentIntent.");
                    System.out.println(event.toJson());
                }
                break;

            case "payment_intent.succeeded":
                if (event.getData().getObject() instanceof PaymentIntent) {
                    PaymentIntent paymentIntent = (PaymentIntent) event.getData().getObject();
                    System.out.println("Payment for " + paymentIntent.getAmount() + " succeeded.");
                    System.out.println(event.toJson());
                } else {
                    System.out.println("payment_intent.succeeded but object is not PaymentIntent.");
                    System.out.println(event.toJson());
                }
                break;

            // use payment_method.attached when expecting a PaymentMethod object
            case "payment_method.attached":
                if (event.getData().getObject() instanceof PaymentMethod) {
                    PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                    System.out.println("Payment method attached: " + paymentMethod.getId());
                    System.out.println(event.toJson());
                } else {
                    System.out.println("payment_method.attached but object is not PaymentMethod.");
                    System.out.println(event.toJson());
                }
                break;

            default:
                System.out.println("Unhandled event type: " + event.getType());
                System.out.println(event.toJson());
                break;
        }

        return ResponseEntity.ok("Received");
    }
}
