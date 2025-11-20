-- Product Detail Information Update (English)
-- Adding benefits, specifications, usage

-- 1. Newpol
UPDATE product_translations
SET
  benefits = '[
    "Removes reactive oxygen species (ROS), inhibits cell damage, and strengthens immune function",
    "Alleviates oxidative stress and heat stress, reduces intestinal inflammation",
    "Improves bone health (bone formation, increased bone density, collagen synthesis promotion)",
    "Promotes vitamin E regeneration and protection, reducing vitamin E usage in feed",
    "Increases growth rate and feed efficiency (G/F), promotes intestinal villi development, improves digestive absorption",
    "Reduces cortisol (stress hormone) levels, significantly decreases mortality and diarrhea rates",
    "Increases beneficial bacteria, reduces harmful bacteria (E.coli, etc.), improves intestinal microbiome balance"
  ]'::jsonb,
  specifications = '{
    "Main Ingredients": "Natural plant-derived polyphenols (Vescalagin, Castalagin, Roburin, Procyanidins, Proanthocyanidins, Resveratrol, Catechins, Theaflavins, Ferulic acid, Quercetin, Epicatechin, Gallic acid, etc.)",
    "Raw Material Sources": "Grapes, green tea, pineapple, star anise, pumpkin seeds, clover, etc.",
    "Application": "All livestock (pigs, chickens, cattle, etc.)",
    "Main Effects": "Antioxidant, intestinal health, growth promotion, immune enhancement"
  }'::jsonb,
  usage = 'Mix evenly into feed. When used as a vitamin E substitute, vitamin E usage can be reduced by up to 50%.'
WHERE product_id = 1 AND language_code = 'en';

-- 2. NAWTAN
UPDATE product_translations
SET
  benefits = '[
    "Promotes growth of beneficial bacteria (Lactobacillus, Bifidobacterium)",
    "Inhibits harmful bacteria (E.coli, Salmonella, Clostridium, etc.), improving intestinal health and animal welfare",
    "Improves feed efficiency, reduces diarrhea and mortality rates, enhances growth rate and egg production",
    "Slows intestinal peristalsis, increasing nutrient and water absorption",
    "Promotes secretion of digestive enzymes such as amylase and trypsin",
    "Reduces moisture and nitrogen in bedding, decreases manure volume, odor, and ammonia emissions",
    "Powerful antioxidant action inhibits free radicals, reduces cell damage and stress"
  ]'::jsonb,
  specifications = '{
    "Main Ingredients": "Natural polyphenols, tannins (chestnut bark extract)",
    "Application Feed": "Premix, compound feed, pellets, extruded feed",
    "Application": "All livestock",
    "Replacement Effect": "Can replace antibiotics and zinc oxide"
  }'::jsonb,
  usage = 'Mix evenly into feed. Ideal for farms looking to reduce antibiotic use, with excellent intestinal health and environmental improvement effects.'
WHERE product_id = 2 AND language_code = 'en';

-- 3. GroPro
UPDATE product_translations
SET
  benefits = '[
    "Digestible protein: Free amino acids and peptides maximize digestibility",
    "Functional nucleic acids: Promote rapid growth and improve metabolic efficiency",
    "Beta-glucan: Activates immune system",
    "Mannan: Binds pathogens and maintains beneficial gut bacteria balance",
    "Improves feed palatability, increasing intake",
    "Supports intestinal health: Promotes gut development, inhibits harmful bacteria",
    "Protein digestibility over 90% for efficient growth support"
  ]'::jsonb,
  specifications = '{
    "Main Ingredients": "Yeast-derived protein, nucleic acids, beta-glucan, mannan",
    "Application": "Weaned piglets",
    "Main Functions": "Growth promotion, immune enhancement, digestive improvement",
    "Replacement Effect": "Can replace SDPP (spray-dried plasma protein)"
  }'::jsonb,
  usage = 'Mix into weaned piglet feed. Can be used as an SDPP substitute, supporting healthy development in early growth stages.'
WHERE product_id = 3 AND language_code = 'en';

-- 4. CH4-King
UPDATE product_translations
SET
  benefits = '[
    "Tannins: Inhibit methane-producing bacteria activity → reduce methane emissions by up to 48%",
    "Quillaja saponins: Suppress protozoa and methanogenic bacteria in rumen, improve energy utilization",
    "Plant saponins: Control microbial balance, bactericidal action → reduce methane production",
    "Organic acids: Promote propionic acid production, improve energy utilization",
    "Probiotics (yeast): Increase productivity and feed efficiency",
    "Seaweed: Inhibit enzymes during digestion, maximize methane reduction efficiency",
    "Fats (oils): Block methane precursors with medium-chain and unsaturated fatty acids"
  ]'::jsonb,
  specifications = '{
    "Main Ingredients": "Tannins, Quillaja saponins, plant saponins, organic acids, probiotics, seaweed, fats",
    "Application": "Ruminants (cattle, sheep, goats, etc.)",
    "Methane Reduction Rate": "Up to 48%",
    "Main Effects": "Reduce methane emissions, improve feed efficiency, enhance animal health"
  }'::jsonb,
  usage = 'Mix into ruminant feed. Essential additive for environmental regulation compliance and sustainable livestock farming.'
WHERE product_id = 4 AND language_code = 'en';

-- 5. Quillaja
UPDATE product_translations
SET
  benefits = '[
    "Increases feed intake and nutrient digestibility",
    "Improves growth rate and health status",
    "Enhances small intestine mucosal cell permeability, promotes digestive enzyme activity",
    "Improves carbohydrate and fat digestibility",
    "Reduces ammonia and hydrogen sulfide gases in excrement → improves barn environment",
    "Reduces methane gas production in ruminants",
    "Immune stimulation, antiviral, antibacterial, antifungal, and antioxidant effects",
    "Promotes antibody and cytokine production in the body, enhances vaccine effectiveness"
  ]'::jsonb,
  specifications = '{
    "Main Ingredients": "Natural saponins extracted from Quillaja tree",
    "Application": "All livestock (pigs, chickens, cattle, fish, shrimp, pets, etc.)",
    "Safety": "100% natural material, safe for humans",
    "Main Effects": "Growth, immunity, digestion, environmental improvement"
  }'::jsonb,
  usage = 'Mix into all livestock feed. Eco-friendly additive applicable to various livestock environments.'
WHERE product_id = 5 AND language_code = 'en';

-- 6. NAWSAN-777
UPDATE product_translations
SET
  benefits = '[
    "Complex organic acid formulation: Inhibits pathogenic bacteria, promotes beneficial bacteria",
    "Ca-formate: Increases protein and dry matter digestibility, improves feed intake and weight gain",
    "Benzoic Acid: Antibacterial effect, reduces ammonia in feces",
    "Fumaric Acid: Regulates feed pH, inhibits pathogens, improves energy and protein digestibility",
    "Citric Acid: Regulates stomach acidity, reduces bacteria",
    "Essential oils: Antibacterial, antioxidant, immune enhancement, promotes feed intake and digestion",
    "Powerful bactericidal action and diarrhea prevention, reduced ammonia emissions"
  ]'::jsonb,
  specifications = '{
    "Main Ingredients": "Complex organic acid formulation, Ca-formate, Benzoic Acid, Fumaric Acid, Citric Acid, essential oils",
    "Application": "All livestock",
    "Main Functions": "Antibiotic replacement, intestinal health, immune enhancement, growth promotion",
    "Environmental Improvement": "Diarrhea prevention, reduced ammonia emissions"
  }'::jsonb,
  usage = 'Mix evenly into feed. Optimal solution for farms looking to reduce antibiotic use.'
WHERE product_id = 6 AND language_code = 'en';

-- 7. Molimprint
UPDATE product_translations
SET
  benefits = '[
    "Metabolic booster: Growth promotion and metabolic activation",
    "Promotes digestion and feed intake: Increases feed consumption, improves digestive capacity",
    "Reduces stress and stabilizes behavior: Improves early growth environment",
    "Antioxidant and immune enhancement: Supports immune function and physiological vitality",
    "Economic benefits: Improves growth rate and reduces production costs",
    "Improves growth efficiency and breeding economics of suckling and weaned piglets"
  ]'::jsonb,
  specifications = '{
    "Main Ingredients": "Star anise oil, cinnamaldehyde, glucoside steviol, citrus extract, thymol, eugenol",
    "Processing Technology": "Complex coacervation (multi-layer coating process)",
    "Application": "Pregnant sows, suckling and weaned piglets",
    "Main Effects": "Maternal imprinting effect, early growth promotion, immune enhancement"
  }'::jsonb,
  usage = 'Feed to pregnant sows to induce maternal imprinting effect, and continue feeding to piglets to promote early feed intake.'
WHERE product_id = 7 AND language_code = 'en';

-- 8. Molimax AE
UPDATE product_translations
SET
  benefits = '[
    "Powerful antibacterial activity: Inhibits pathogenic E.coli, Salmonella, Clostridium, Campylobacter, etc.",
    "Promotes digestion and feed intake: Stabilizes intestinal environment, increases digestive efficiency",
    "Immune enhancement: Antioxidant and immune-boosting effects",
    "Palatability improvement: Enhances feed taste and aroma, increasing intake",
    "Improved stability: Microencapsulation maintains effectiveness in feed and intestinal environments",
    "Reduced skin irritation: Improved safety compared to free-form essential oils"
  ]'::jsonb,
  specifications = '{
    "Main Ingredients": "Natural essential oils (plant extracts)",
    "Processing Technology": "Complex coacervation microencapsulation (1st fat layer + 2nd protein coating layer)",
    "Application": "All livestock",
    "Main Effects": "Antibacterial, digestive promotion, immune enhancement, palatability improvement"
  }'::jsonb,
  usage = 'Mix evenly into feed. Microencapsulation technology ensures stable effectiveness in the intestines.'
WHERE product_id = 8 AND language_code = 'en';

-- 9. NAW-SWEET 3
UPDATE product_translations
SET
  benefits = '[
    "Promotes feed intake: Stimulates taste, increases early consumption and intake in piglets",
    "Improves digestibility: Promotes digestive enzyme secretion, increases nutrient absorption efficiency",
    "Minimizes climate impact: Maintains stable feed intake even in hot summer",
    "Sow benefits: Prevents weight loss, advances return to estrus, increases milk production",
    "Growth promotion: Alleviates post-weaning growth depression through adequate feed intake",
    "Shortens time to reach market weight"
  ]'::jsonb,
  specifications = '{
    "Main Ingredients": "Ultra-fine sweetener, high-intensity sweetener, sweetness maximizer",
    "Application": "Weaned piglets, growing-finishing pigs, sows, chickens, calves",
    "Product Characteristics": "Reduces unpleasantness with masking effect, improves cloudy taste of single saccharin",
    "Main Effects": "Increased feed intake, growth promotion, sow weight management"
  }'::jsonb,
  usage = 'Mix into weaned piglet, sow, chicken, and calf feed. Particularly effective for improving feed intake in newly weaned piglets and during hot summers.'
WHERE product_id = 9 AND language_code = 'en';
