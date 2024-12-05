(ns core
  (:require
   [clojure.string :as str]))

(defn compose [& fs]
  (apply comp (reverse fs)))

(defn split-whitespace [s]
  (str/split s #"\s+"))

(defn get-match-groups [regex s]
  (->> s (re-seq regex) (map rest)))

(defn re-pos [re s]
  (loop [m (re-matcher re s)
         res {}]
    (if (.find m)
      (recur m (assoc res (.start m) (.group m)))
      res)))
