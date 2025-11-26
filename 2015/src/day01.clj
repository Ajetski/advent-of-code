(ns day01
  (:require input-manager))

(def input (input-manager/get-input-raw 2015 01))

(def up? #{\(})
(def down? #{\)})

;; part 1
(-
 (count (filter up? input))
 (count (filter down? input)))

;; part 2
(loop [i 0
       floor 0]
  (cond
    (< floor 0) i

    (up? (.charAt input i))
    (recur (inc i)
           (inc floor))

    :else
    (recur (inc i)
           (dec floor))))

