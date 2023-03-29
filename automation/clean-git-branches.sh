#!/bin/bash

repository_names=("twobit" "sweater")
src="src"

if [ ! -d "$src" ]; then
  mkdir -p "$src"
else
  rm -rf "$src"/*
fi

cd "$src"

for repository in "${repository_names[@]}"; do
  git_repository="git@github.com:bombey77/${repository}.git"
  git clone "$git_repository"
  echo "Cloned from $git_repository"
  cd "$repository"

  for branch in $(git branch -r | grep -vE "master|main"); do
    if [ -z "$(git log -1 --since='1 day ago' -s "$branch")" ]; then
      remote_branch=$(echo "$branch" | sed 's#origin/##')
      echo "Branch name to remove - $remote_branch"
#      git push origin -d "$remote_branch"
    fi
  done

  cd ..
  # rm -rf "$repository"
done

cd ..
# rm -rf "$src"