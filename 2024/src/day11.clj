(ns day11
  (:require
   [clojure.string :as str]
   [core :as c]
   [input-manager]))

(def input (->>  (input-manager/get-input 11)
                 first
                 c/split-whitespace
                 (map parse-long)
                 frequencies))

(defn split-num [n]
  (let [s (str n)
        m (count s)]
    (map parse-long (map str/join (split-at (int (/ m 2)) s)))))

(defn incr-count [coll k amnt]
  (update coll k #(if (nil? %)
                    amnt
                    (+ % amnt))))

(defn update-counts [cnts]
  (reduce
   (fn [cnts' n]
     (let [cnt (cnts n)]
       (cond
         (= 0 n) (incr-count cnts' 1 cnt)

         (even? (count (str n)))
         (let [[a b] (split-num n)]
           (-> (incr-count cnts' a cnt)
               (incr-count b cnt)))

         :else (incr-count cnts' (* n 2024) cnt))))
   {}
   (keys cnts)))

(defn ans [n]
  (->> (range n)
       (reduce (fn [acc _] (update-counts acc))
               input)
       (map second)
       (reduce +)))

(ans 25)
(ans 75)
