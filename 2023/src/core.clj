(ns core
  (:require [clj-http.client :as client]
            [clojure.string :as string]))

(def input-cache (atom {}))
(def cookie (str "session=" (slurp "session")))

(defn get-puzzle-input [day]
  (or (@input-cache day)
      (swap! input-cache assoc day
             (-> (str "https://adventofcode.com/2023/day/" day "/input")
                 (client/get {:throw-entire-message? true
                              :headers {"Cookie" cookie}})
                 :body
                 string/split-lines))))

(defn re-seq-pos [pattern string]
  (let [m (re-matcher pattern string)]
    ((fn step []
       (when (. m find)
         (cons {:start (. m start) :end (. m end) :group (. m group)}
               (lazy-seq (step))))))))

(comment
  input-cache)
