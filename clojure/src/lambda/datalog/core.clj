(ns datalog.core
  (:require [datascript.core :as ds]))

(def schema {:parents {:db/cardinality :db.cardinality/many}})

;;; Facts

(def facts
  [{:name "bob" ; name(E, "bob")
    :sex :male}
   {:name "alice"
    :sex :female}
   {:name "charlie"
    :sex :male
    :parents #{"bob", "alice"}}
   {:name "dave"
    :sex :male
    :parents #{"charlie"}}])

;; Populate DB with facts
(def db
  (-> (ds/empty-db schema) (ds/db-with facts)))

;;; Rules

(def rules
  ['[(parent ?parent ?child)
     [?e :parents ?parent]
     [?e :name ?child]]

   '[(mother ?parent ?child)
     [?c :name ?child]
     [?c :parents ?parent]
     [?p :name ?parent]
     [?p :sex :female]]

   '[(father ?parent ?child)
     [?c :name ?child]
     [?c :parents ?parent]
     [?p :name ?parent]
     [?p :sex :male]]

   '[(ancestor ?parent ?child)
     (parent ?parent ?child)]
   '[(ancestor ?x ?y)
     (parent ?x ?z)
     (ancestor ?z ?y)]])

;;; Queries

;; Is Bob a parent of Charlie?
(ds/q
 '[:find ?p .
   :in $ % ?p ?c
   :where (parent ?p ?c)]
 db rules "bob" "charlie")

;; Who are Charlie's parents?
(ds/q
 '[:find [?p ...]
   :in $ % ?c
   :where (parent ?p ?c)]
 db rules "charlie")

;; Who's Charlie's mother?
(ds/q
 '[:find [?p ...]
   :in $ % ?c
   :where (mother ?p ?c)]
 db rules "charlie")

;; Who Bob is the father of?
(ds/q
 '[:find [?c ...]
   :in $ % ?p
   :where (father ?p ?c)]
 db rules "bob")

;; Who Bob is the mother of?
(ds/q
 '[:find [?c ...]
   :in $ % ?p
   :where (mother ?p ?c)]
 db rules "bob")

;; Is Bob an ancestor of Dave?
(ds/q
 '[:find [?p ...]
   :in $ % ?p ?c
   :where (ancestor ?p ?c)]
 db rules "bob" "dave")

;; Is Alice an ancestor of Dave?
(ds/q
 '[:find [?p ...]
   :in $ % ?p ?c
   :where (ancestor ?p ?c)]
 db rules "alice" "dave")

;; Is Charlie an ancestor of Dave?
(ds/q
 '[:find [?p ...]
   :in $ % ?p ?c
   :where (ancestor ?p ?c)]
 db rules "charlie" "dave")

;; Is Dave an ancestor of Dave?
(ds/q
 '[:find [?x ...]
   :in $ % ?x ?y
   :where (ancestor ?x ?y)]
 db rules "dave" "dave")

;; Who Dave's ancestors are?
(ds/q
 '[:find [?x ...]
   :in $ % ?y
   :where (ancestor ?x ?y)]
 db rules "dave")

;; Who Dave is an ancestor of?
(ds/q
 '[:find [?y ...]
   :in $ % ?x
   :where (ancestor ?x ?y)]
 db rules "dave")
