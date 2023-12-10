(ns day10
  (:require
   [clojure.math :as math]
   [core :refer [get-puzzle-input]]))

(def input (get-puzzle-input 10))

(def pipe-out-types {:left #{\- \7 \J \S}
                     :right #{\- \F \L \S}
                     :up #{\| \L \J \S}
                     :down #{\| \7 \F \S}})

(def char-map
  (->> input
       (map-indexed (fn [row line]
                      (map-indexed (fn [col c]
                                     [[row col] c])
                                   line)))
       (apply concat)
       (into {})))

(def opposite-dir {:left :right
                   :right :left
                   :up :down
                   :down :up})

(def start (->> char-map
                (filter #(= \S (second %)))
                ffirst))

(defn follow-dir [loc dir]
  (condp = dir
    :up (update loc 0 dec)
    :down (update loc 0 inc)
    :left (update loc 1 dec)
    :right (update loc 1 inc)))

(def char-dir-map {\| [:up :down]
                   \- [:left :right]
                   \L [:right :up]
                   \F [:right :down]
                   \J [:left :up]
                   \7 [:left :down]})

(defn follow-pipe [loc from]
  (let [c (char-map loc)]
    (if (and (not (nil? c))
             (not= \. c))
      (let [dir (->> c
                     (char-dir-map)
                     (filter #(not= from %))
                     (first))
            new-loc (follow-dir loc dir)]
        (if ((pipe-out-types (opposite-dir dir)) (char-map new-loc))
          [new-loc dir]
          nil))
      nil)))

;; part 1
(->>  (for [start-dir [:up :down :left :right]]
        (loop [loc (follow-dir start start-dir)
               cnt 0
               from (opposite-dir start-dir)
               visited #{}]
          (if (or (= loc nil) (= loc start))
            cnt
            (let [res (follow-pipe loc from)]
              (if (or (= res nil)
                      (visited (first res)))
                nil
                (recur (first res)
                       (inc cnt)
                       (opposite-dir (second res))
                       (conj visited loc)))))))
      (filter (comp not nil?))
      (map #(/ % 2))
      (map math/round)
      (into #{})
      (apply max))

;; part 2
(let [path (->>  (for [start-dir [:right :left :up :down]]
                   (loop [loc (follow-dir start start-dir)
                          route []
                          from (opposite-dir start-dir)
                          visited #{}]
                     (if (or (= loc nil) (= loc start))
                       route
                       (let [res (follow-pipe loc from)]
                         (if (or (= res nil)
                                 (visited (first res)))
                           nil
                           (recur (first res)
                                  (conj route loc)
                                  (opposite-dir (second res))
                                  (conj visited loc)))))))
                 (filter (comp not nil?))
                 (sort-by #(/ (count %) 2) >)
                 first
                 (into #{})
                 (#(conj % start)))
      by-row (->> path
                  (group-by first)
                  (map #(update % 1 (partial map second)))
                  (map #(update % 1 sort))
                  (into {}))
      coords (->> char-map
                  (map first)
                  (filter (comp not path)))]
  (->> coords
       (filter (fn [[row col]]
                 (->> row
                      by-row
                      (take-while #(> col %))
                      (map #(char-map [row %]))
                      (filter (pipe-out-types :up))
                      (count)
                      odd?)))
       count))

