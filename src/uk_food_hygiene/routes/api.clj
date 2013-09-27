(ns uk-food-hygiene.routes.api
  (:use compojure.core)
  (:use noir.response)
  (:require [uk-food-hygiene.util :as util]
            [uk-food-hygiene.models.db :as db]))

(defroutes api-routes
  (GET "/business-types" []
       (json (db/find-business-type {})))
  (GET ["/business-types/:id" :id #"[0-9]+"] [id]
       (json (first (db/find-business-type {:id (Integer. id)})))))
