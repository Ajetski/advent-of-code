(ns day19
  (:require [clojure.string :as str]
            input-manager))

(let [[w _ & p]
      (input-manager/get-input 2024 19)]
  (def words (into #{} (str/split w #", ")))
  (def puzzles p)
  (def word-sizes (->> words (map count) distinct)))

(defn valid-combination-count
  ([puzzle]
   (valid-combination-count puzzle 0 1 (mapv (constantly 0) puzzle)))
  ;; dp is vec of ints which contains the count of combinations for substring from puzzle[0] and ending at puzzle[n]
  ([puzzle pos weight dp]
   (if (>= pos (count puzzle))
     (last dp)
     (let [dp' (->> (map (partial + pos) word-sizes)
                    (filter (partial >= (count puzzle))) ;; prevent NPE on substring overflow
                    (map (partial subs puzzle pos))
                    (filter words)
                    (map (comp (partial + pos) dec count))
                    (reduce (fn [acc idx] (update acc idx (partial + weight)))
                            dp))]
       (recur puzzle (inc pos) (get dp' pos) dp')))))

;; part 1 solution
(->> (map valid-combination-count puzzles)
     (filter (partial < 0))
     count)

;; part 2 solution
(->> (map valid-combination-count puzzles) (apply +))
