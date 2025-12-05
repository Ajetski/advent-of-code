(ns day05
  (:require input-manager))

(let [[ranges [_ & nums]] (->> (input-manager/get-input 2025 5)
                               (split-with (partial not= "")))]
  (def ranges (sort (mapv (comp #(mapv parse-long %)
                                #(re-seq #"\d+" %))
                          ranges)))
  (def nums (mapv parse-long nums)))

(defn fresh? [num]
  (some (fn [[a b]]
          (<= a num b))
        ranges))

;; part 1
(count (filter fresh? nums))

;; part 2
(->> ranges
     (reduce (fn [[fresh-cnt max-seen :as acc]
                  [a b]]
               (cond (>= max-seen b) acc
                     (>= max-seen a) [(+ fresh-cnt (- b max-seen)) b]
                     :else [(+ fresh-cnt (inc (- b a))) b]))
             [0 0])
     first)

