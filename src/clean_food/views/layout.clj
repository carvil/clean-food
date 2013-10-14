(ns clean-food.views.layout
  (:use noir.request)
  (:require [selmer.parser :as parser]))

(def template-path "clean_food/views/templates/")

(defn render [template & [params]]
  (parser/render-file (str template-path template)
                      (assoc params :servlet-context (:context *request*))))

