(ns uk-food-hygiene.views.layout
  (:use noir.request)
  (:require [selmer.parser :as parser]))

(def template-path "uk_food_hygiene/views/templates/")

(defn render [template & [params]]
  (parser/render-file (str template-path template)
                      (assoc params :servlet-context (:context *request*))))

