(ns uk-food-hygiene.models.db
  (:use korma.core
        [korma.db :only (defdb)]))

(def db-spec {:subprotocol "postgresql"
         :subname "//localhost/uk_food_hygiene"
         :user "admin"
         :password ""})

(defdb db db-spec)
