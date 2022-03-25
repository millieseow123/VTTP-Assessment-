package nus.iss.edu.sg.SSF_Assessment.Assessment.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import nus.iss.edu.sg.SSF_Assessment.Assessment.Model.Quotation;
import nus.iss.edu.sg.SSF_Assessment.Assessment.Service.QuotationService;

@RestController
@RequestMapping("api/po")
public class PurchaseOrderRestController {

    @Autowired
    public QuotationService QuotationSvc;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> postPO(@RequestBody String payload) throws IOException {

        JsonObject body;
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            jakarta.json.JsonReader reader = Json.createReader(is);
            body = reader.readObject();
        } catch (Exception ex) {
            body = Json.createObjectBuilder()
                    .add("error", ex.getMessage())
                    .build();
            return ResponseEntity.internalServerError().body(body.toString());
        }

        // Task4 & 6
        List<String> item = new LinkedList<String>();
        JsonArray lineItems = body.getJsonArray("lineItems");

        double total = 0.0;
        Quotation quotation = null;
        for (int i = 0; i < lineItems.size(); i++) {
            JsonObject object = lineItems.getJsonObject(i);
            item.add(object.getString("item"));
            int quantity = object.getInt("quantity"); //qty of fruit

        
        quotation = (QuotationSvc.getQuotations(item)).get(); 
        for (int j = 0; j < item.size(); j++) {
            String nameOfFruit = item.get(j);// name of fruit
            float fruitPrice = quotation.getQuotation(nameOfFruit);// price of each fruit
        for(int k=0; k<item.size(); k++){
            float priceOfEachFruit = (quantity * fruitPrice);
            total += priceOfEachFruit;
        System.out.println("total " + total);   }      
        }}

        JsonObject o = Json.createObjectBuilder()
            .add("invoiceId:", quotation.getQuoteId())
            .add("name", body.getString("name"))
            .add("total", total).build();

        return ResponseEntity.ok(o.toString());
    }

}
