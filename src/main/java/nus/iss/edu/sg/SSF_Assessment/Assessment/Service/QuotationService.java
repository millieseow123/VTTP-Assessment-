package nus.iss.edu.sg.SSF_Assessment.Assessment.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import nus.iss.edu.sg.SSF_Assessment.Assessment.Model.Quotation;

@Service
public class QuotationService {
    public Optional<Quotation> getQuotations(List<String> items) throws IOException{

        JsonArrayBuilder itemsInArray = Json.createArrayBuilder();
            items.stream().forEach(i -> itemsInArray.add(i));
            JsonArray jsonArrayOfItems = itemsInArray.build();

//System.out.println("is it an array now?!?!" + jsonArrayOfItems);

    RequestEntity<String> req = RequestEntity
        .post("https://quotation.chuklee.com/quotation")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(jsonArrayOfItems.toString(), String.class);


    RestTemplate template = new RestTemplate();
    ResponseEntity<String> resp = null;
    try {    
        resp = template.exchange(req, String.class);
    } catch (Exception e) {
        System.err.printf("Failed");
        return Optional.empty();
        //TODO: handle exception
    }

    JsonObject data = null;
    try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())){
        JsonReader reader = Json.createReader(is);
        data = reader.readObject();
    }

//System.out.println("resppp1" + data);
    
    Quotation quoteResp = new Quotation();
    quoteResp.setQuoteId(data.getString("quoteId"));

    Map<String, Float> mapOfQuotations = new HashMap<String, Float>();
    JsonArray arrayQuotation =  data.getJsonArray("quotations");

    for(int i = 0; i<arrayQuotation.size(); i++){
        JsonObject xx = arrayQuotation.getJsonObject(i);
        String nameOfItem = xx.getString("item");
        Float unitPrice2 = Float.parseFloat((xx.get("unitPrice")).toString());
        mapOfQuotations.put(nameOfItem, unitPrice2);
    
//System.out.println(">>>" + mapOfQuotations);     
    }
    quoteResp.setQuotations(mapOfQuotations);
//System.out.println(">>>quoteresponse" + quoteResp);  
    try { 
        return Optional.of(quoteResp);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return Optional.empty();

}
    }
        


