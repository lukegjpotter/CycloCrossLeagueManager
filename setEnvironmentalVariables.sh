#!/usr/bin/env bash

# Test Colours
red=`tput setaf 1`
green=`tput setaf 2`
reset=`tput sgr0`

# Ensure that file is being sourced, as ./ won't set the Env Variables outside this script's context.
if [ "$0" = "./setEnvironmentalVariables.sh" ]
then
  echo " ${red}[error]  Usage of this file should be 'source setEnvironmentalVariables.sh'${reset}"
  exit 1
fi

# Set GOOGLE_SHEETS_API_KEY for local testing
echo " [info] Setting GOOGLE_SHEETS_API_KEY"
export GOOGLE_SHEETS_API_KEY='please-update-me'
echo " ${green}[info] Set GOOGLE_SHEETS_API_KEY to $GOOGLE_SHEETS_API_KEY ${reset}"