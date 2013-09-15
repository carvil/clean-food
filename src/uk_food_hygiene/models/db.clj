(ns uk-food-hygiene.models.db
  (:use korma.core
        [korma.db :only (defdb)]))

(def db-spec {:subprotocol "postgresql"
         :subname "//localhost/uk_food_hygiene"
         :user "admin"
         :password ""})

(defdb db db-spec)

(def local-authority-keys [:code :business_id :name :website :email])
(def business-type-keys   [:business_type_id :name])

; CREATE

(defn create-rating [rating]
  (insert :ratings
          (values rating)))

(defn create-local-authority [local-authority]
  (insert :local_authorities
          (values local-authority)))

(defn create-business-type [business-type]
  (insert :business_types
          (values business-type)))

(defn create-establishment [establishment]
  (insert :establishments
          (values establishment)))

; SELECT

(defn get-establishment [id]
  (first (select :establishments
                 (where {:id id})
                 (limit 1))))

(defn get-rating [id]
  (first (select :ratings
                 (where {:id id})
                 (limit 1))))

(defn get-local-authority [id]
  (first (select :local_authorities
                 (where {:id id})
                 (limit 1))))

(defn find-local-authority
  [where-map]
  (select :local_authorities
          (where where-map)))

(defn get-business-type [id]
  (first (select :business_types
                 (where {:id id})
                 (limit 1))))

(defn find-business-type
  [where-map]
  (select :business_types
          (where where-map)))

