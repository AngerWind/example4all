import js from "@eslint/js";
import globals from "globals";
import json from "@eslint/json";
import markdown from "@eslint/markdown";
import css from "@eslint/css";
import shell from "@eslint/shell";
// eslint-config-prettier: 关闭所有与 Prettier 冲突的格式化类规则, eslint只用于语法检查, 代码格式使用prettier（必须放在数组最后，才能覆盖前面的配置）
import prettier from "eslint-config-prettier";
import {defineConfig, globalIgnores} from "eslint/config";

export default defineConfig([
    // 全局忽略的文件, 这些文件不需要lint（package-lock.json 由 npm 自动生成，且其根键 "" 会触发 json 规则误报）
    globalIgnores(["dist/", "coverage/", "*.min.js", "package-lock.json"]),
    {
        files: ["**/*.{js,mjs,cjs}"],
        plugins: {js},
        extends: ["js/recommended"],
        languageOptions: {globals: {...globals.browser, ...globals.node}},
        linterOptions: {
            // 最佳实践1: 禁用所有内联配置注释（如 /* eslint ... */、/* eslint-disable */）
            // 团队统一在配置文件中管理规则，防止个人在源码里随意绕过规则
            noInlineConfig: true,
            // 最佳实践2/6: 多余的 eslint-disable 注释直接报错（禁用了本来就不会报错的规则）
            // 防止代码演进后残留无用的禁用注释
            reportUnusedDisableDirectives: "error",
            // 最佳实践3: 多余的内联配置注释报错（注释内容与配置文件已设置的完全一致，没有产生任何改变）
            reportUnusedInlineConfigs: "error",
        },
    },
    {files: ["**/*.json"], plugins: {json}, language: "json/json", extends: ["json/recommended"]},
    {files: ["**/*.jsonc"], plugins: {json}, language: "json/jsonc", extends: ["json/recommended"]},
    {files: ["**/*.json5"], plugins: {json}, language: "json/json5", extends: ["json/recommended"]},
    {files: ["**/*.md"], plugins: {markdown}, language: "markdown/gfm", extends: ["markdown/recommended"]},
    {files: ["**/*.css"], plugins: {css}, language: "css/css", extends: ["css/recommended"]},
    {files: ["**/*.sh", "**/*.bash"], plugins: {shell}, language: "shell/bash", extends: ["shell/recommended"]},
    // 最佳实践4: 格式化交给 Prettier，此处关闭 ESLint 中所有与之冲突的格式化规则（必须放最后, 才能覆盖之前的配置）
    prettier,
]);
