(ns core)

(defn compose [& fs]
  (apply comp (reverse fs)))
