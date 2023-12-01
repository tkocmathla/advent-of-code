(ns build
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b]))

(def lib 'net.clojars.aoc-clj/aoc-clj)
(def version "0.1.0-SNAPSHOT")
(def class-dir "target/classes")
