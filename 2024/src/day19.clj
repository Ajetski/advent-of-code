(ns day19
  (:require [clojure.string :as str]
            input-manager))

(def word-sizes (memoize (comp distinct (partial map count))))

(defn increment-counts-for-matching-words [words puzzle counts pos]
  (let [weight (if (= pos 0)
                 1
                 (get counts (dec pos)))]
    (->> (map (partial + pos) (word-sizes words))
         (filter (partial >= (count puzzle))) ;; prevent NPE on substring overflow
         (map (partial subs puzzle pos))
         (filter words)
         (map (comp (partial + pos) dec count))
         (reduce (fn [acc idx] (update acc idx (partial + weight)))
                 counts))))

(defn valid-combination-count [words puzzle]
  (->> (range 0 (count puzzle))
       (reduce (partial increment-counts-for-matching-words words puzzle)
               (mapv (constantly 0) puzzle))
       last))

(let [[w _ & p] (input-manager/get-input 2024 19)]
  (def words (into #{} (str/split w #", ")))
  (def puzzles p))

;; part 1 solution
(->> (map (partial valid-combination-count words) puzzles)
     (filter (partial < 0))
     count)

;; part 2 solution
(apply + (map (partial valid-combination-count words) puzzles))

(comment
  (increment-counts-for-matching-words #{"a"} "atest"  [0 0 0 0 0 0] 0) ; [1 0 0 0 0 0]
  (increment-counts-for-matching-words #{"a"} "btest"  [0 0 0 0 0 0] 0) ; [0 0 0 0 0 0]
  (increment-counts-for-matching-words #{"te"} "atest" [1 0 0 0 0 0] 1) ; [1 0 1 0 0 0]

  (increment-counts-for-matching-words #{"wo" "rd"} "word" [0 1 0 0] 2) ; [0 1 0 1]
  (valid-combination-count #{"wo" "rd"} "word") ; 1
  (valid-combination-count #{"word"} "word")    ; 1

  (increment-counts-for-matching-words #{"wo" "or" "w" "rd" "d"} "word" [0 0 0 0] 0) ; [1 1 0 0]
  (valid-combination-count #{"wo" "or" "w" "rd" "d"} "word") ; 2, because ("wo" "r" "d") and ("w" "or" "d")
  )
