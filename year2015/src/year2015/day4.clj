(ns year2015.day4
  (:require
    [clojure.java.io :as io]
    [clojure.set :as set]
    [clojure.string :as string])
  (:import [java.security MessageDigest]))

(defonce input "ckczppom")

(defn md5 [k]
  (->> (.digest (MessageDigest/getInstance "MD5") (.getBytes k))
       (BigInteger. 1)
       (format "%032x")))

(defn solve [k n]
  (->> (iterate (fn [[i x]] [(inc i) (md5 (str k i))]) [0 "nil"])
       (drop-while (comp (partial not-every? #{\0}) (partial take n) second))
       ffirst dec))

(def p1 #(solve input 5))
(def p2 #(solve input 6))

(comment
  (= 117946 (p1 input))
  (= 3938038 (p2 input)))
