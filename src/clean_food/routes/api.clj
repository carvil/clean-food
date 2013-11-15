(ns clean-food.routes.api
  (:use compojure.core
        noir.response
        ring.middleware.basic-authentication
        [environ.core :refer [env]])
  (:require [clean-food.util :as util]
            [clean-food.models.db :as db]))

(def defaults {:offset 0 :limit 20})

(defn not-present?
  [element]
  (or (nil? element) (empty? element)))

(defn authenticated? [name pass]
  (and (= name (env :auth-user "clean-food"))
       (= pass (env :auth-pass ""))))

(defroutes api-routes*
  ; Business Types
  (GET "/business-types" []
    (json (db/find-business-type {})))

  (GET ["/business-types/:id" :id #"[0-9]+"] [id]
    (json (first (db/find-business-type {:id (Integer. id)}))))

  ; Local Authorities
  (GET "/local-authorities" []
    (json (db/find-local-authority {})))

  (GET ["/local-authorities/:id" :id #"[0-9]+"] [id]
    (json (first (db/find-local-authority {:id (Integer. id)}))))

  (GET ["/local-authorities/:id/establishments" :id #"[0-9]+"] [id]
    (json (db/find-establishment {:local_authority_id (Integer. id)})))

  ; Establishments
  (GET "/establishments" {params :params}
    (let [options (merge-with #(if (not-present? %1) %2 %1) params defaults)]
      (json
        (db/find-establishment {}
                               (:limit options)
                               (:offset options)))))

  (GET ["/establishments/:fhrs_id" :fhrs_id #"[0-9]+"] [fhrs_id]
    (json (first (db/find-establishment {:fhrs_id fhrs_id}))))

  (GET ["/establishments/:fhrs_id/rating" :fhrs_id #"[0-9]+"] [fhrs_id]
    (let [establishment (first (db/find-establishment {:fhrs_id fhrs_id}))
          rating        (db/get-rating (:rating_id establishment))]
      (json rating))))

(def api-routes
  (-> #'api-routes*
    (wrap-basic-authentication authenticated?)))
