@echo off
setlocal enabledelayedexpansion

set GITEE=https://gitee.com/swunjavaclass/finalwork.git
set GITHUB=https://github.com/luolangaga/finalwork.git

echo ========================================
echo   图书馆管理系统 - 双推送脚本
echo ========================================
echo.

if not "%~1"=="" (
    set BRANCH=%~1
) else (
    set BRANCH=master
)
echo 目标分支: !BRANCH!
echo.

echo [1/2] 推送至 Gitee...
git push origin !BRANCH!
if !errorlevel! neq 0 (
    echo [警告] Gitee 推送失败
) else (
    echo [完成] Gitee 推送成功
)

echo.
echo [2/2] 推送至 GitHub...
git push github !BRANCH!
if !errorlevel! neq 0 (
    echo [警告] GitHub 推送失败
) else (
    echo [完成] GitHub 推送成功
)

echo.
echo ========================================
echo   完成
echo ========================================
pause
