(ns migrations.20130818181033-rating
  (:use [korma.db]
        [korma.core])
  (:require [clojure.java.jdbc :as sql]
            [uk-food-hygiene.models.db :as db]))

(defn up
  "Migrates the database up to version 20130818181033."
  []
  (println "migrations.20130818181033-rating up...")
  (sql/with-connection db/db-spec
    (sql/create-table
      :rating
        [:id                      "SERIAL PRIMARY KEY"]
        [:value                    :integer]
        [:key                      "varchar(20)"]
        [:date                     :date]
        [:hygiene                  :integer]
        [:structural               :integer]
        [:confidence_in_management :integer]
        [:created_at  "TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()"])))

(defn down
  "Migrates the database down from version 20130818181033."
  []
  (println "migrations.20130818181033-rating down...")
  (sql/with-connection db/db-spec
    (sql/drop-table :rating)))
