package com.burlington.arcade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
public class SalesDataController {

    @GetMapping("/property-stats")
    public ResponseEntity<?> getPropertyStats() {
        return ResponseEntity.ok(Map.of(
            "founded", 1819, "length_yards", 196, "boutiques", 46,
            "annual_visitors_millions", 4, "location", "51 Piccadilly, Mayfair W1J 0QJ",
            "world_first", "World's first modern shopping mall",
            "media_impressions_pa", "12M+", "press_mentions_pa", "500+"
        ));
    }

    @GetMapping("/demographics")
    public ResponseEntity<?> getDemographics() {
        return ResponseEntity.ok(Map.of(
            "annual_visitors", "4,000,000+",
            "visitor_split", Map.of("international", 62, "domestic_uk", 38),
            "top_nationalities", List.of("American","Gulf/Middle East","French","Italian","Japanese","Chinese"),
            "wealth_profile", "Top 5% global HNWI & UHNWI",
            "avg_dwell_time_minutes", 38,
            "repeat_visitor_rate_pct", 67,
            "proximity", Map.of(
                "Bond Street","2 min walk","Royal Academy of Arts","Adjacent",
                "Savile Row","3 min walk","The Ritz Hotel","5 min walk",
                "Buckingham Palace","10 min walk"
            )
        ));
    }

    @GetMapping("/leasing/categories")
    public ResponseEntity<?> getLeasingCategories() {
        return ResponseEntity.ok(List.of(
            Map.of("id","luxury","label","Luxury Flagship","icon","💎",
                "tagline","The definitive London address for global maisons",
                "size_sqft","400 – 1,200","term","7 – 15 years",
                "floor_position","Ground floor, Piccadilly frontage",
                "current_tenants", List.of("Hancocks","Manolo Blahnik","Lalique","Bell & Ross"),
                "pitch","A flagship on Burlington Arcade is not a retail unit. It is a declaration. In 205 years, only the world's finest houses have earned this address. Footfall here is prequalified — every visitor arrives with intent.",
                "advantages", List.of(
                    "Original Regency shopfront — unmatched heritage value",
                    "4M annual visitors, 62% international HNWI",
                    "Beadle escort creates unrivalled client arrival experience",
                    "Adjacent to Royal Academy of Arts programming",
                    "Press & editorial amplification on every new opening"
                ),
                "availability","By invitation — enquire for current opportunities"
            ),
            Map.of("id","retail","label","Contemporary Luxury","icon","✦",
                "tagline","For the next generation of luxury brands",
                "size_sqft","200 – 600","term","3 – 10 years",
                "floor_position","Ground & first floor",
                "current_tenants", List.of("N.Peal Cashmere","Church's","Code8","Kick Game"),
                "pitch","Burlington Arcade actively seeks bold, contemporary voices that respect the heritage whilst redefining it. This tier offers the credibility of the arcade's address with the flexibility to grow.",
                "advantages", List.of(
                    "Association with 205 years of curatorial excellence",
                    "Access to the Arcade's global PR & media network",
                    "Flexible fit-out within Regency envelope guidelines",
                    "Cross-referral from established luxury neighbours",
                    "VIP client hosting in the Arcade's private spaces"
                ),
                "availability","2 units available Q2 2025 — enquire now"
            ),
            Map.of("id","fnb","label","Food & Lifestyle","icon","🥐",
                "tagline","Dining as destination, not afterthought",
                "size_sqft","150 – 400","term","3 – 7 years",
                "floor_position","Ground floor, arcade-facing",
                "current_tenants", List.of("Ladurée","Noxy Brothers","Charbonnel et Walker"),
                "pitch","The Arcade's food and lifestyle offer is intentionally curated and severely limited — meaning each F&B presence becomes a genuine destination within the destination.",
                "advantages", List.of(
                    "Captive audience of the highest-spending shoppers in London",
                    "Unique licence for morning café trade (arcade opens 8am weekdays)",
                    "Corporate gifting & private commission opportunities",
                    "Seasonal programming partnership with the Arcade",
                    "Delivery exclusions — in-arcade experience only"
                ),
                "availability","1 unit under consideration — enquire for details"
            ),
            Map.of("id","popup","label","Pop-Up & Activation","icon","⚡",
                "tagline","Test. Launch. Amplify.",
                "size_sqft","80 – 300","term","1 week – 12 months",
                "floor_position","Flexible — multiple locations",
                "current_tenants", List.of("Mr Porter Private Appointment","Seasonal brand activations"),
                "pitch","The world's most prestigious test-and-learn environment. Launch a new product, enter the UK market, or create an unmissable cultural moment — with the Arcade's full reach behind you.",
                "advantages", List.of(
                    "Fastest path to Mayfair presence with minimal commitment",
                    "Full PR & social amplification included in package",
                    "Access to the Arcade's established HNW client database",
                    "Option to convert to permanent lease if mutual fit confirmed",
                    "Flexible build-out — bespoke installation or branded kiosk"
                ),
                "availability","Rolling availability — 4–6 week lead time"
            )
        ));
    }

    @GetMapping("/events/capabilities")
    public ResponseEntity<?> getEventCapabilities() {
        return ResponseEntity.ok(List.of(
            Map.of("id","buyout","name","Full Arcade Buyout","icon","🥂",
                "description","Exclusive use of all 196 yards for a private evening. The Beadles manage guest arrival in full ceremonial uniform.",
                "capacity_reception",500,"capacity_seated",120,
                "hire_window","18:00 – 01:00",
                "includes", List.of(
                    "All 46 shopfronts available for brand installation",
                    "Beadle ceremonial greeting at Piccadilly entrance",
                    "Exclusive use of underground passageways for VIP access",
                    "Dedicated load-in from Burlington Gardens",
                    "PA system integration with Regency acoustics"
                ),
                "lead_time_weeks",8,"status","Available","price_from","POA"
            ),
            Map.of("id","windows","name","Window Showcase Campaign","icon","🖼️",
                "description","Transform some or all 46 windows into a curated brand narrative. 11,000+ daily pedestrians pass through.",
                "windows_available",46,"duration_range","1 week – 3 months",
                "foot_traffic_daily","11,000+",
                "includes", List.of(
                    "Opening night press preview event",
                    "Behind-the-glass installation lighting rig",
                    "Social media content creation by Arcade team",
                    "QR-linked digital storytelling panels",
                    "Press outreach to Vogue, Tatler, Harper's Bazaar"
                ),
                "lead_time_weeks",4,"status","Available","price_from","From £28,000/week"
            ),
            Map.of("id","heritage","name","Heritage VIP Tour","icon","👑",
                "description","Hosted by Head Beadle Mark Lord. Access Victorian underground passageways and 205 years of untold stories.",
                "max_guests",30,"duration_minutes",75,
                "includes", List.of(
                    "Private access to underground passageways",
                    "Champagne reception post-tour",
                    "Framed archival 1819 Arcade blueprint print",
                    "Private shopping hour with preferred boutiques",
                    "Certificate of Heritage Experience"
                ),
                "lead_time_weeks",2,"status","Available","price_from","From £3,500"
            ),
            Map.of("id","installation","name","Cultural Installation","icon","🎭",
                "description","Partner with the Royal Academy of Arts for cross-programmed cultural moments — art, light, sound, and performance.",
                "formats", List.of("Sculpture in the arcade","Projected light installation after hours","Photography exhibition across windows","Live music — strings, harp, chamber"),
                "RA_partnership",true,"media_value_estimate","£250,000+ earned media per installation",
                "lead_time_weeks",12,"status","Select dates available","price_from","POA"
            )
        ));
    }

    @GetMapping("/events/highlights")
    public ResponseEntity<?> getEventHighlights() {
        return ResponseEntity.ok(List.of(
            Map.of("title","Queen's Platinum Jubilee Showcase","year",2022,"type","Royal Celebration",
                "description","A curated jewellery showcase celebrating 70 years of Her Majesty's passion for gemstones. All 46 windows dressed in royal blue and gold.",
                "impressions","8.2M media impressions","tags", List.of("Royal","Heritage","Jewellery")),
            Map.of("title","Burlington Arcade 200th Anniversary","year",2019,"type","Heritage Celebration",
                "description","A year-long programme marking two centuries of continuous trading — archival exhibitions, limited-edition collaborations, and a royal ceremony.",
                "impressions","11M media impressions","tags", List.of("Anniversary","Heritage","Cultural")),
            Map.of("title","The Jaguar Heist Re-enactment","year",2018,"type","Brand Activation",
                "description","A theatrical reimagining of the 1964 robbery — masked men in a Jaguar through the gates — to launch a luxury watchmaker's 'Daring' campaign.",
                "impressions","5.4M global impressions","tags", List.of("Watches","Theatrical","PR")),
            Map.of("title","Netflix Period Drama Production","year",2023,"type","Film & Television",
                "description","Featured as primary exterior location in a major Netflix period drama. 50+ total production credits make Burlington Arcade London's most filmed luxury interior.",
                "impressions","Ongoing — 50+ credits","tags", List.of("Film","Television","Location"))
        ));
    }

    @GetMapping("/sponsorship/tiers")
    @PreAuthorize("hasAnyRole('SALES','ADMIN')")
    public ResponseEntity<?> getSponsorshipTiers() {
        return ResponseEntity.ok(List.of(
            Map.of("tier",1,"name","Event Partner","duration","Single event","recommended",false,
                "assets", List.of(
                    "Full arcade exclusivity for one private evening",
                    "Bespoke guest experience design",
                    "Catering & entertainment rights",
                    "Brand integration across all event comms",
                    "Beadle ceremonial escort for all guests",
                    "Post-event press release & social coverage"
                ),
                "media_value_est","£120,000+ earned media","audience_reach","Up to 500 guests + post-event media"
            ),
            Map.of("tier",2,"name","Seasonal Sponsor","duration","3 months","recommended",false,
                "assets", List.of(
                    "Themed arcade dressing for full season",
                    "Brand at all primary audience touchpoints",
                    "Joint press release & social media campaign",
                    "1 exclusive private evening event",
                    "Co-branded seasonal gift guide (digital & print)",
                    "Access to the Arcade's HNW client mailing list",
                    "Press preview event with media attendance"
                ),
                "media_value_est","£380,000+ earned media","audience_reach","1M+ visitors during campaign period"
            ),
            Map.of("tier",3,"name","Presenting Partner","duration","Annual","recommended",true,
                "assets", List.of(
                    "Exclusive naming rights to annual signature campaign",
                    "Beadle uniform co-branding for campaign duration",
                    "All 46 window displays for campaign narrative",
                    "4 exclusive private evening events per year",
                    "Full digital & press co-marketing programme",
                    "First right of refusal on all leasing opportunities",
                    "Cross-programming with Royal Academy of Arts",
                    "Annual heritage tour for 30 top clients",
                    "Dedicated editorial feature in Vogue UK partnership",
                    "Year-round presence on arcade.com & socials"
                ),
                "media_value_est","£1.2M+ earned media","audience_reach","4M visitors + 12M social impressions"
            )
        ));
    }

    @GetMapping("/sponsorship/audience-data")
    public ResponseEntity<?> getSponsorshipAudienceData() {
        return ResponseEntity.ok(Map.of(
            "visitor_profile", Map.of(
                "HNWI_percentage",71,"avg_basket_value_gbp",2400,
                "international_pct",62,"repeat_visit_pct",67
            ),
            "media_footprint", Map.of(
                "social_impressions_pa","12M+","press_mentions_pa","500+",
                "vogue_uk_features_pa",4,"film_tv_credits_total","50+"
            ),
            "notable_shoppers_historical", List.of("HM Queen Elizabeth II","Marilyn Monroe","Grace Kelly","Sophia Loren","Sir Paul McCartney"),
            "film_appearances", List.of("Skyfall","The Crown","Downton Abbey","Bridgerton")
        ));
    }

    @GetMapping("/venues")
    public ResponseEntity<?> getVenues() {
        return ResponseEntity.ok(List.of(
            Map.of("id","arcade-main","name","The Grand Arcade","icon","🏛️",
                "tagline","196 yards of living history",
                "capacity_cocktail",500,"capacity_seated",120,
                "features", List.of("Original 1819 Regency glass-and-iron roof","Natural skylight throughout","Polished British stone flooring","46 illuminated shopfront windows","Integrated PA capability","Climate controlled year-round"),
                "ideal_events", List.of("Luxury product launches","Brand anniversary galas","Fashion week presentations","Corporate award ceremonies"),
                "status","Available nightly from 18:00"
            ),
            Map.of("id","underground","name","The Victorian Vaults","icon","🕯️",
                "tagline","Where 205 years of secrets are kept",
                "capacity_cocktail",40,"capacity_seated",20,
                "features", List.of("Original Victorian basement architecture","Cast-iron 1819 heating stoves","Atmospheric brick vaulting","Underground passageway system","Total privacy","Beadle escort access only"),
                "ideal_events", List.of("UHNWI private dinners","Board-level corporate retreats","Collector previews","Documentary filming"),
                "status","By private arrangement only"
            ),
            Map.of("id","piccadilly","name","The Piccadilly Gateway","icon","✦",
                "tagline","London's most prestigious threshold",
                "capacity_cocktail",80,"capacity_seated",0,
                "features", List.of("1911 Benjamin Clemens sculpted facade","Dual Beadle sentinel positions","Full Piccadilly pedestrian visibility","Historic wrought-iron gates","Adjacent Bond Street traffic","Drone & live camera permitted"),
                "ideal_events", List.of("Product reveal moments","Red carpet arrivals","Brand film exterior shoots","Press launch photography"),
                "status","Available 06:00–09:00 or post 19:00"
            ),
            Map.of("id","royal-academy","name","Royal Academy Partnership","icon","🎨",
                "tagline","Art. Commerce. Culture. Unified.",
                "capacity_cocktail",800,"capacity_seated",350,
                "features", List.of("Combined Burlington + Royal Academy audience","Cross-ticketed events programming","World-class gallery spaces adjacent","Shared VIP client entertainment","Joint editorial partnerships — Vogue, The Art Newspaper"),
                "ideal_events", List.of("Art-world brand activations","Cultural sponsorship launches","Collector evening programmes","Architecture & design events"),
                "status","Partnership arrangement — enquire"
            )
        ));
    }

    @PostMapping("/enquiry")
    public ResponseEntity<?> submitEnquiry(@RequestBody Map<String, String> enquiry) {
        String name = enquiry.getOrDefault("name", "");
        String company = enquiry.getOrDefault("company", "");
        String type = enquiry.getOrDefault("type", "general");
        String ref = "BA-" + System.currentTimeMillis();
        return ResponseEntity.ok(Map.of(
            "status","received","reference",ref,"type",type,
            "message","Thank you " + name + " from " + company + ". Our commercial team will respond within 24 hours.",
            "next_steps", List.of(
                "Commercial Director review within 4 business hours",
                "Tailored opportunity pack sent to your email",
                "Site visit & private consultation arranged",
                "Heads of terms issued within 5 working days of meeting"
            )
        ));
    }
}
