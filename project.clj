(defproject clean-food "0.1.0-SNAPSHOT"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [lib-noir "0.6.8"]
                 [compojure "1.1.5"]
                 [ring-server "0.2.8"]
                 [selmer "0.4.0"]
                 [com.taoensso/timbre "2.5.0"]
                 [com.postspectacular/rotor "0.1.0"]
                 [com.taoensso/tower "1.7.1"]
                 [markdown-clj "0.9.29"]
                 [postgresql/postgresql "9.1-901.jdbc4"]
                 [korma "0.3.0-RC5"]
                 [environ "0.4.0"]
                 [org.clojure/data.json "0.2.3"]
                 [log4j "1.2.17" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]]

  :ring {
    :handler clean-food.handler/war-handler,
    :init clean-food.handler/init,
    :destroy clean-food.handler/destroy
  }

  :profiles {
    :production {
      :ring {
        :open-browser? false, :stacktraces? false, :auto-reload? false
      }
    },
    :dev {
      :dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.1.8"]]
    }
  }

  :url "http://example.com/FIXME"
  :description "Visualisation of London's food hygiene statistics according to
    the open data provided by data.gov.uk"

  :plugins [[lein-ring "0.8.6"]
            [drift "1.5.2"]
            [lein-environ "0.4.0"]]

  ; Entry point for the parser
  :main clean-food.parser.core

  :min-lein-version "2.0.0")
