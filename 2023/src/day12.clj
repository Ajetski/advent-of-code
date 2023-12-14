(ns day12
  (:require
   [clojure.string :as str]
   [core :refer [get-puzzle-input]]))

(defn parse-line [line] (-> (str/split line #" ")
                            (update 1 #(->> (str/split % #",")
                                            (mapv parse-long)))))

(defn make-anso []
  (with-local-vars
   [ans (memoize
         (fn f [dots blocks i bi current]
           (let [len-blocks (count blocks)
                 dot_i (when (< i (count dots)) (get dots i))]
             (if (= i (count dots))
               (cond (and (= bi len-blocks)
                          (= current 0))
                     1
                     (and (= bi (dec len-blocks))
                          (= current (get blocks bi)))
                     1
                     :else
                     0)
               (->> [\. \#]
                    (map #(if (or (= dot_i %)
                                  (= dot_i \?))
                            (cond (and (= % \.)
                                       (= current 0))
                                  (f dots blocks (inc i) bi 0)

                                  (and (= % \.)
                                       (> current 0)
                                       (< bi len-blocks)
                                       (= (get blocks bi) current))
                                  (f dots blocks (inc i) (inc bi) 0)

                                  (= % \#)
                                  (f dots blocks (inc i) bi (inc current))

                                  :else 0)
                            0))
                    (reduce +))))))]
    (.bindRoot ans @ans)
    @ans))

(->> (get-puzzle-input 12)
     (map parse-line)
     (map #((make-anso) (first %) (second %) 0 0 0))
     (reduce +))

(->> (get-puzzle-input 12)
     (map parse-line)
     (mapv (fn [[s cnts]]
             [(str/join (interpose "?" (repeat 5 s)))
              (vec (apply concat (repeat 5 cnts)))]))
     (pmap #((make-anso) (first %) (second %) 0 0 0))
     (reduce +))

