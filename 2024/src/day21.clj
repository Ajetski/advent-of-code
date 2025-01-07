(ns day21
  (:require
   [clojure.math :as math]
   [clojure.string :as str]
   [core :as c :refer :all]
   [input-manager :as i]
   [clojure.math.combinatorics :as combo]))

(def input (i/get-input 2024 21))

(def pad-1-data [[nil \0 \A]
                 [\1 \2 \3]
                 [\4 \5 \6]
                 [\7 \8 \9]])
(def pad-locs (-> pad-1-data map-to-coords (dissoc nil)))
(def pad-locs-by-coords (-> pad-1-data map-by-coords (dissoc [0 0])))

(def pad-2-data [[\< \v \>]
                 [nil \^ \A]])
(def pad-locs-2 (-> pad-2-data map-to-coords (dissoc nil)))
(def pad-locs-2-by-coords (-> pad-2-data map-by-coords (dissoc [1 0])))

(def start (pad-locs \A))
(def start-2 (pad-locs-2 \A))

(defn manhattan-distance [[row1 col1] [row2 col2]]
  (+ (abs (- row1 row2))
     (abs (- col1 col2))))

(defn char->step-fn [c]
  (fn [[row col]]
    ({\> [row (inc col)]
      \< [row (dec col)]
      \^ [(inc row) col]
      \v [(dec row) col]} c)))

(defn progressive-steps [[row col :as pos] [target-row target-col] grid]
  (->> [(when (> target-row row)
          \^)
        (when (< target-row row)
          \v)
        (when (> target-col col)
          \>)
        (when (< target-col col)
          \<)]
       (filter identity)
       (filter #(grid ((char->step-fn %) pos)))))

(defn shortest-walks
  "returns list of all minimal length list keypresses (arrow chars)"
  [start-pos target-pos grid]
  (loop [n 0
         walks #{[start-pos []]}]
    (or (and (or (->> walks
                      (map first)
                      (filter #(= % target-pos))
                      not-empty)
                 (empty? walks))
             (->> walks
                  (map second)
                  (map #(conj % \A))))
        (->> walks
             (mapcat (fn [[loc keystrokes]]
                       (let [steps (progressive-steps loc target-pos grid)]
                         (->> steps
                              (map #(vector ((char->step-fn %) loc)
                                            (conj keystrokes %)))))))
             set
             (recur (inc n))))))

(defn generate-robot-subwalks [start-pos walk]
  (->> walk
       (map pad-locs-2)
       ((juxt identity rest))
       (apply map vector)
       (#(conj % [start-pos (pad-locs-2 (first walk))]))
       (map #(shortest-walks (first %) (second %) pad-locs-2-by-coords))))

(defn generate-all-subwalk-options [depth code]
  (loop [n 1
         acc (->> code
                  (reduce (fn [{:keys [pos w]}
                               el]
                            (let [pos' (pad-locs el)
                                  walks (shortest-walks pos pos' pad-locs-by-coords)]
                              {:pos pos'
                               :w (conj w walks)}))
                          {:pos start
                           :w []})
                  :w)]
    (if (= n depth)
      acc
      (recur (inc n)
             (n-map (* n 2) #(generate-robot-subwalks (pad-locs-2 \A) %) acc)))))

(defn get-count-from-subwalks [depth all-walks]
  (loop [n (dec depth)
         acc (n-map (* depth 2) count all-walks)]
    (if (neg? n)
      acc
      (recur (dec n)
             (->> acc
                  (n-map (inc (* n 2))
                         (partial apply min))
                  (n-map (* n 2)
                         (partial reduce +)))))))

(defn get-ans-for-code [depth code]
  (let [c (->> (generate-all-subwalk-options depth code)
               (get-count-from-subwalks depth))
        n (parse-long (->> code
                           drop-last
                           str/join))]
    (* c n)))

;; part 1
(->> input
     (map (partial get-ans-for-code 3))
     (reduce +)
     println)
