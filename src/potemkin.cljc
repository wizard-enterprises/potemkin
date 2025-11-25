(ns potemkin
  (:require
    [potemkin.namespaces]
    [potemkin.macros]
    [potemkin.utils]))

(potemkin.namespaces/import-vars potemkin.namespaces/import-vars) ;; totally meta

(import-vars
  [potemkin.namespaces

   import-fn
   import-macro
   import-def]

  [potemkin.macros

   unify-gensyms
   normalize-gensyms
   equivalent?]

  [potemkin.utils

   condp-case
   doit
   doary])
