(ns day02
  (:require [input-manager]))

(defn parse-line
  "s is a string in the form of lxwxh, whee l, w, and h are integral patterns"
  [s]
  (map parse-long (re-seq #"\d+" s)))

(def input (->> (input-manager/get-input 2015 2)
                (map parse-line)))

(defn smallest-side [[l w h]]
  (min (* l w)
       (* h w)
       (* l h)))

(defn paper-needed [[l w h :as args]]
  (+ (* 2 (+ (* l w) (* w h) (* h l)))
     (smallest-side args)))

;; part 1
(->> input
     (map paper-needed)
     (apply +))

(defn smallest-perimeter [[l w h]]
  (* 2 (min (+ l w)
            (+ h w)
            (+ l h))))

(defn volume [[l w h]]
  (* l w h))

;; part 2
(->> input
     (map (juxt smallest-perimeter volume))
     (map #(apply + %))
     (apply +))
