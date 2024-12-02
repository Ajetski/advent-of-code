(ns core
  (:require
   [clojure.string :as str]))

(defn compose [& fs]
  (apply comp (reverse fs)))

(defn split-whitespace [s]
  (str/split s #"\s+"))

(defn get-match-groups [regex s]
  (->> s (re-seq regex) first rest))
