@echo off
REM Push to both Gitee and GitHub simultaneously
echo === Pushing to Gitee (origin) ===
git push origin master %*
if %errorlevel% neq 0 (
    echo [WARN] Gitee push failed, continuing...
)
echo.
echo === Pushing to GitHub ===
git push github master %*
if %errorlevel% neq 0 (
    echo [WARN] GitHub push failed
)
echo.
echo === Done ===
