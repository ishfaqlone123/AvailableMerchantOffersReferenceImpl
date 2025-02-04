package com.mastercard.offers;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.OffersApi;
import org.openapitools.client.api.ReferenceDataApi;
import org.openapitools.client.model.Offer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping(method = RequestMethod.POST)
public class AvailableOffersController {

    @Autowired
    private ApplicationProps props;
    private static final Logger log = LoggerFactory.getLogger(AvailableOffersController.class);

    @GetMapping("/productTypes")
    public String getProductTypes(Model model) {
        try {
            ApiClient client = new ApiClient();
            RequestSigner.signRequest(client, props);
            log.info("Signing Client: " + client);

            ReferenceDataApi api = new ReferenceDataApi(client);
            model.addAttribute("productTypes", api.getProductTypes("java","application/json"));
        } catch (Exception e) {
            log.error("Error: "+e);
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "productTypes";
    }

    @GetMapping("offerCategories")
    public String getOfferCategories(Model model) {
        try {
            ApiClient client = new ApiClient();
            RequestSigner.signRequest(client, props);
            log.info("Signing Client: " + client);

            ReferenceDataApi api = new ReferenceDataApi(client);
            model.addAttribute("offerCategories", api.getOfferCategories("java", "application/json"));
        } catch (Exception e) {
            log.error("Error: " + e);
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "offerCategories";
    }

    @GetMapping("/searchOffers")
    public String searchOffers(Model model) {
        try {
            ApiClient client = new ApiClient();
            RequestSigner.signRequest(client, props);
            log.info("Signing Client: " +client);

            ReferenceDataApi api = new ReferenceDataApi(client);
            model.addAttribute("productTypes", api.getProductTypes("java","application/json"));
            model.addAttribute("offerCategories", api.getOfferCategories("java", "application/json"));
        } catch (ApiException e) {
            log.error("API Exception: "+e);
            model.addAttribute("error", e.getMessage());
            return "error";
        } catch (Exception e) {
            log.error("Error: "+e);
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "searchOffers";
    }

    /**
     * Get All available offers
     * @param latitude
     * @param longitude
     * @param productType
     * @param categoryCode
     * @param radius
     * @param offset
     * @param limit
     * @param sort
     * @param unitSystem
     * @param searchText
     * @param model
     * @return
     */
    @GetMapping("offers")
    public String getOffers(@RequestParam(name="latitude", required=true) String latitude,
                            @RequestParam(name="longitude", required=true) String longitude,
                            @RequestParam(name="productType", required=true) String productType,
                            @RequestParam(name="categoryCode", required=false) String categoryCode,
                            @RequestParam(name="radius", required=false) String radius,
                            @RequestParam(name="offset", required=false) String offset,
                            @RequestParam(name="limit", required=false) String limit,
                            @RequestParam(name="sort", required=false) String sort,
                            @RequestParam(name="unitSystem", required=false) String unitSystem,
                            @RequestParam(name="searchText", required=false) String searchText,
                            Model model) {
        try {
            ApiClient client = new ApiClient();

            RequestSigner.signRequest(client, props);
            log.info("Signing Client: " + client);

            OffersApi api = new OffersApi(client);
            List<Offer> offers = api.getMerchantOffers(new BigDecimal(latitude), new BigDecimal(longitude), productType, null,
                    "java", "application/json", categoryCode, null,
                    null, null, sort, unitSystem, searchText);

            model.addAttribute("empty", offers.isEmpty());
            model.addAttribute("offers", offers);
        }  catch (ApiException e) {
            log.error("API Exception: "+e);
            model.addAttribute("error", e.getMessage());
            return "error";
        } catch (Exception e) {
            log.error("Error: " + e);
            model.addAttribute("error", e);
            return "error";
        }

        return "offers";
    }
}