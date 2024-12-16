(ns day14
  (:require
   [core :as c]
   [input-manager :refer [get-input]]
   [clojure.string :as str]))

(def input (->> (get-input 14)
;                 (str/split-lines "p=0,4 v=3,-3
; p=6,3 v=-1,-3
; p=10,3 v=-1,2
; p=2,0 v=2,-1
; p=0,0 v=1,3
; p=3,0 v=-2,-2
; p=7,6 v=-1,-3
; p=3,0 v=-1,-2
; p=9,3 v=2,3
; p=7,3 v=-1,2
; p=2,4 v=2,-3
; p=9,5 v=-3,-3")
                (map #(c/get-match-groups #"(-?\d+)" %))
                (c/mmmap parse-long)
                (mapv (comp vec flatten))))

(def height (inc (apply max (map second input))))
(def width (inc (apply max (map first input))))

(def mid-row (int (/ height 2)))
(def mid-col (int (/ width 2)))

(defn loc [[px py vx vy] n]
  [(mod (+ px (* vx n)) width)
   (mod (+ py (* vy n)) height)])

(defn get-quadrants [locs]
  [(filter (fn [[x y]]
             (and (< x mid-col)
                  (< y mid-row))) locs)
   (filter (fn [[x y]]
             (and (> x mid-col)
                  (< y mid-row))) locs)
   (filter (fn [[x y]]
             (and (< x mid-col)
                  (> y mid-row))) locs)
   (filter (fn [[x y]]
             (and (> x mid-col)
                  (> y mid-row))) locs)])

;; part 1
(->> input
     (map #(loc % 100))
     get-quadrants
     (map count)
     (reduce *))

(defn print-map [i]
  (let [locs (set (mapv #(loc % i) input))]
    ; (println locs)
    (map (fn [y]
           (apply str (map
                       #(if (contains? locs [% y])
                          \#
                          \space)
                       (range width))))

         (range height))))

(->> (range 10000 15000)
     (mapv #(do
              (c/log (str "\n idx:" % "\n"))
              (c/log (str/join "\n" (print-map %)))))
     first)
