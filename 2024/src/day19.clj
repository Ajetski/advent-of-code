(ns day19
  (:require
   [clojure.string :as str] ; [core :as c]
   input-manager
   [orchard.pp :as pp]))

(def sample-input "r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb")

(let [[w _ & p]
      #_(str/split-lines sample-input)
      (input-manager/get-input 2024 19)]
  (def words (into #{} (str/split w #", ")))
  (def puzzles p)
  (def word-sizes (->> words
                       (map count)
                       distinct
                       sort
                       reverse)))

(defn p [x]
  (println x)
  x)

(defn next-search-index [puzzle searched valid-subsets]
  (if (every? (complement true?) searched)
    0
    (some->> valid-subsets
             (map-indexed vector)
             (filter second)
             (filter (fn [[idx :as el]]
                       (not (searched (inc idx)))))
             last
             first
             inc)))

(defn find-valid-subsets [puzzle search-idx valid-subsets]
  (let [max-idx (count puzzle)]
    (->> word-sizes
         (map (partial + search-idx)) ; get end pos
         (filter #(<= % max-idx))
         (map (partial subs puzzle search-idx)) ;get substrings
         (filter words)
         (map (comp (partial + search-idx) dec count))
         (reduce (fn [acc idx]
                   (assoc acc idx true))
                 valid-subsets))))

(defn is-valid
  ([puzzle]
   (is-valid puzzle
             (mapv (constantly false) puzzle)
             (mapv (constantly false) puzzle)
             0))
  ([puzzle searched valid-subsets n]
   (let [search-idx (next-search-index puzzle searched valid-subsets)]
     (cond
       (or (not search-idx)
           (= search-idx -1))
       false

       (>= search-idx (count puzzle))
       true

       :else
       (let [next-subsets (find-valid-subsets puzzle search-idx valid-subsets)]
         (if (last next-subsets)
           true
           (recur puzzle
                  (assoc searched search-idx true)
                  next-subsets
                  (inc n))))))))

;; part 1 solution
(->> puzzles
     (map is-valid)
     (filter identity)
     count)

(defn valid-combination-count
  ([puzzle]
   (valid-combination-count puzzle
                            0
                            1
                            (mapv (constantly 0) puzzle)))
  ;; dp is vec of ints which contains the count of combinations for substring from puzzle[0] and ending at puzzle[n]
  ([puzzle pos weight dp]
   (if (>= pos (count puzzle))
     (last dp)
     (let [dp' (->> (map (partial + pos) word-sizes)
                    (filter (partial >= (count puzzle)))
                    (map (partial subs puzzle pos))
                    (filter words)
                    (map (comp dec count))
                    (map (partial + pos))
                    (reduce (fn [acc idx] (update acc idx (partial + weight)))
                            dp))]
       (recur puzzle
              (inc pos)
              (or (get dp' pos) 0)
              dp')))))

;; part 2 solution
(->> puzzles
     (map valid-combination-count)
     (apply +))
