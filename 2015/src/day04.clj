(ns day04
  (:require [input-manager]
            [clojure.set])
  (:import java.security.MessageDigest java.math.BigInteger))

(def input (input-manager/get-input-raw 2015 4))

(defn string->md5 [s]
  (let [algorithm (MessageDigest/getInstance "MD5")
        raw (.digest algorithm (.getBytes s))]
    (format "%032x" (BigInteger. 1 raw))))

(defn solution [target-prefix]
  (loop [i 0]
    (let [md5 (string->md5 (str input i))]
      (if (.startsWith md5 target-prefix)
        i
        (recur (inc i))))))

(solution "00000")
(solution "000000")
