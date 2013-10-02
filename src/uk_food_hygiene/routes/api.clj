(ns uk-food-hygiene.routes.api
  (:use compojure.core)
  (:use noir.response)
  (:require [uk-food-hygiene.util :as util]
            [uk-food-hygiene.models.db :as db]))

(defroutes api-routes
           ;
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
  (GET ["/establishments/:fhrs_id" :fhrs_id #"[0-9]+"] [fhrs_id]
    (json (first (db/find-establishment {:fhrs_id fhrs_id}))))

  (GET ["/establishments/:fhrs_id/rating" :fhrs_id #"[0-9]+"] [fhrs_id]
    (let [establishment (first (db/find-establishment {:fhrs_id fhrs_id}))
          rating        (db/get-rating (:rating_id establishment))]
      (json rating)))
  )
