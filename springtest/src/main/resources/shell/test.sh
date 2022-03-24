#!/bin/bash

a=100
b=200

function exchange
{
  local oldvar=$1 #a
  local newvar=$2 #b

  local oldval=${!oldvar} #100
  local newval=${!newvar} #200


  if [[ -n "${oldval}" && -n "$newval" ]]; then
    # shellcheck disable=SC2086

    eval ${newvar}=\"${oldval}\" #

    # shellcheck disable=SC2086
    eval ${oldvar}=\"${newval}\"
  fi
}
function hadoop_add_entry
{
  if [[ ! ${!1} =~ \ ${2}\  ]] ; then
    #shellcheck disable=SC2140
    eval "${1}"=\""${!1} ${2} "\"
  fi
}

exchange a b

e=100
c=20
hadoop_add_entry e c
echo $e$c

#echo $a$b