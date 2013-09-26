(ns uk-food-hygiene.parser.establishment
  (use [uk-food-hygiene.parser.utils :as utils]
       [uk-food-hygiene.models.db    :as db]
       [clj-time.format              :as cljtime]
       [clj-time.coerce              :only [to-sql-date]]))

; Local Authority

(def local-authority-xml-keys [:LocalAuthorityCode :LocalAuthorityBusinessID
                               :LocalAuthorityName :LocalAuthorityWebSite
                               :LocalAuthorityEmailAddress])

(defn local-authority-values
  "Extract all the local authority values"
  [establishment-data]
  (map
    #(utils/find-tag-content establishment-data %)
    local-authority-xml-keys))

(defn fetch-local-authority-data
  "Extracts the necessary local authority attributes from an establishment"
  [establishment-data]
  (zipmap db/local-authority-keys (local-authority-values establishment-data)))

(defn find-or-create-local-authority
  "Finds or creates an authority from a full dataset"
  [establishment-data]
  (let [code (utils/find-tag-content establishment-data :LocalAuthorityCode)
        local-authority (first
                          (db/find-local-authority {:code code}))]
    (if (= local-authority nil)
      (db/create-local-authority
        (fetch-local-authority-data establishment-data))
      local-authority)))

; Business Type

(def business-type-xml-keys [:BusinessTypeID :BusinessType])

(defn business-type-values
  "Extract all the business type values"
  [establishment-data]
  (map
    #(utils/find-tag-content establishment-data %)
    business-type-xml-keys))

(defn fetch-business-type-data
  "Extracts the necessary business type attributes from an establishment"
  [establishment-data]
  (zipmap db/business-type-keys (business-type-values establishment-data)))

(defn find-or-create-business-type
  "Finds or creates a business type from a full dataset"
  [establishment-data]
  (let [business-id (utils/find-tag-content establishment-data :BusinessTypeID)
        business-type (first (db/find-business-type
                               {:business_type_id business-id}))]
    (if (= business-type nil)
      (db/create-business-type
        (fetch-business-type-data establishment-data))
      business-type)))

; Rating

(defn to-date
  "Converts 'YYYY-MM-DD' into a Date object"
  [date-str]
  (if (nil? date-str)
    nil
    (to-sql-date (parse (formatter "yyyy-MM-dd") date-str))))

(defn find-or-create-rating-for-establishment
  "Creates a rating in the database from a full dataset"
  [establishment-data]
  (let [scores     (utils/find-all-by-tag establishment-data :Scores)
        value      (utils/find-tag-content establishment-data :RatingValue)
        ratingKey  (utils/find-tag-content establishment-data :RatingKey)
        date       (utils/find-tag-content establishment-data :RatingDate)
        hygiene    (utils/find-tag-content scores :Hygiene)
        structural (utils/find-tag-content scores :Structural)
        confidence (utils/find-tag-content scores :ConfidenceInManagement)]
    (db/create-rating {:value value :key ratingKey :date (to-date date)
                       :hygiene hygiene :structural structural
                       ::confidence_in_management confidence})))

; Establishment

(def establishment-xml-keys [:FHRSID :BusinessName :AddressLine1
                             :AddressLine2 :AddressLine3 :PostCode
                             :SchemeType])

(defn establishment-values
  "Extract all the establishment basic values"
  [establishment-data]
  (map
    #(utils/find-tag-content establishment-data %)
    establishment-xml-keys))

(defn fetch-establishment-data
  "Extracts the necessary establishment attributes"
  [establishment-data]
  (zipmap db/establishment-keys (establishment-values establishment-data)))

(defn fetch-geo-location
  "Extract the geolocation data from an establishment"
  [establishment-data]
  (let [location (utils/find-all-by-tag establishment-data :Geocode)
        lat      (utils/find-tag-content location :Latitude)
        lng      (utils/find-tag-content location :Longitude)]
    { :location (db/to-point lat lng) }))

(defn create-establishment-record
  "Creates an establishment with the relations"
  [establishment-data authority business-type rating]
  (let [fhrs_id (utils/find-tag-content establishment-data :FHRSID)
        establishment (first
                        (db/find-establishment {:fhrs_id fhrs_id}))]
    (if (nil? establishment)
      (db/create-establishment
        (merge (fetch-establishment-data establishment-data)
               (fetch-geo-location establishment-data)
               {:rating_id          (get rating :id)
                :business_type_id   (get business-type :id)
                :local_authority_id (get authority :id)}))
      establishment)))

(defn process
  "Parses an establishment and inserts into the database"
  [establishment]
  (let [authority     (find-or-create-local-authority establishment)
        business-type (find-or-create-business-type establishment)
        rating        (find-or-create-rating-for-establishment establishment)]
    (create-establishment-record
      establishment authority business-type rating)))

(defn parse-establishments
  "parses an XML node containing establishments"
  [establishments]
  (doseq [establishment (:content establishments)]
    (process (:content establishment))))

(defn parse-doc
  "Parses an XML document containing food hygiene data"
  [uri]
  (-> uri
      utils/load-xml-file
      :content
      second
      parse-establishments))
