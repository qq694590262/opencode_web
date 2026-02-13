#!/bin/bash
# 自动推送到 Gitee 和 GitHub

# 推送到 Gitee
git push gitee master 2>/dev/null &

# 推送到 GitHub  
git push github master 2>/dev/null &

wait

echo "已推送到 Gitee 和 GitHub"
