#!/bin/sh

# after pull from git://git.code.sf.net/p/lejos/ev3
git filter-branch --force --index-filter 'git rm --cached --ignore-unmatch kernel/linux-03.20.00.13_20121214.tgz' --prune-empty --tag-name-filter cat -- --all
git commit --amend -CHEAD
rm -rf .git/refs/original/ && git reflog expire --all &&  git gc --aggressive --prune
echo "now you can push to github"