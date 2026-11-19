// 为中文分片标题追加英文原标题
// 配对规则：按顺序配对两个文件中代码块之外的 markdown 标题
// 跳过：已含 （ 的中文标题、与英文完全相同的中文标题
const fs = require("fs");
const path = require("path");

const base = "D:/code/example4all/frontend/example4eslint/eslint-docs-zh";
const pairs = [
	["zh-sections/01-getting-started.md", "en/getting-started.md"],
	["zh-sections/01b-core-concepts.md", "en/core-concepts.md"],
	["zh-sections/02-configure-index.md", "en/configure-index.md"],
	["zh-sections/03-configuration-files.md", "en/configuration-files.md"],
	["zh-sections/04-language-options.md", "en/language-options.md"],
	["zh-sections/05-rules.md", "en/rules.md"],
	["zh-sections/06-plugins.md", "en/configure-plugins.md"],
	["zh-sections/07-parser.md", "en/parser.md"],
	["zh-sections/08-combine-configs.md", "en/combine-configs.md"],
	["zh-sections/09-ignore.md", "en/ignore.md"],
	["zh-sections/10-debug.md", "en/debug.md"],
	["zh-sections/11-migration-guide.md", "en/migration-guide.md"],
	["zh-sections/20-extend-index.md", "en/index.md"],
	["zh-sections/21-ways-to-extend.md", "en/ways-to-extend.md"],
	["zh-sections/22-plugins.md", "en/plugins.md"],
	["zh-sections/22b-custom-rule-tutorial.md", "en/custom-rule-tutorial.md"],
	["zh-sections/22c-custom-rules.md", "en/custom-rules.md"],
	["zh-sections/22d-code-path-analysis.md", "en/code-path-analysis.md"],
	["zh-sections/22e-selectors.md", "en/selectors.md"],
	["zh-sections/22f-scope-manager-interface.md", "en/scope-manager-interface.md"],
	["zh-sections/22g-rule-deprecation.md", "en/rule-deprecation.md"],
	["zh-sections/22h-custom-processors.md", "en/custom-processors.md"],
	["zh-sections/22i-languages.md", "en/languages.md"],
	["zh-sections/22j-plugin-migration-flat-config.md", "en/plugin-migration-flat-config.md"],
	["zh-sections/23-shareable-configs.md", "en/shareable-configs.md"],
	["zh-sections/24-custom-formatters.md", "en/custom-formatters.md"],
	["zh-sections/25-custom-parsers.md", "en/custom-parsers.md"],
	["zh-sections/26-stats.md", "en/stats.md"],
];

function extractHeadings(lines, stripFrontmatter) {
	const hs = [];
	let fence = false,
		fm = false;
	let start = stripFrontmatter;
	for (let i = 0; i < lines.length; i++) {
		const l = lines[i];
		if (start && i === 0 && l.trim() === "---") {
			fm = true;
			continue;
		}
		if (fm) {
			if (l.trim() === "---") {
				fm = false;
				start = false;
			}
			continue;
		}
		if (/^```/.test(l)) {
			fence = !fence;
			continue;
		}
		if (!fence && /^#{1,6} /.test(l)) hs.push({ i, text: l.replace(/^#{1,6} /, "").trim() });
	}
	return hs;
}

let problems = 0;
for (const [zhRel, enRel] of pairs) {
	const zhPath = path.join(base, zhRel);
	const zhLines = fs.readFileSync(zhPath, "utf8").split("\n");
	const enLines = fs.readFileSync(path.join(base, enRel), "utf8").split("\n");
	const zhH = extractHeadings(zhLines, false);
	const enH = extractHeadings(enLines, true);

	// 中文分片第一个标题是文档大标题（英文标题在 frontmatter 中），不参与配对
	let zhList = zhH;
	if (zhH.length === enH.length + 1 && zhH[0].text.includes("（")) {
		zhList = zhH.slice(1);
	}
	if (zhList.length !== enH.length) {
		console.log(`!! 数量不一致 ${zhRel}: zh=${zhH.length} en=${enH.length}`);
		console.log("   zh:", zhH.map((h) => h.text).join(" | "));
		console.log("   en:", enH.map((h) => h.text).join(" | "));
		problems++;
		continue;
	}
	let changed = 0;
	for (let k = zhList.length - 1; k >= 0; k--) {
		const z = zhList[k],
			e = enH[k];
		const zLine = zhLines[z.i];
		if (zLine.includes("（")) continue; // 已有英文括号
		if (z.text === e.text) continue; // 相同（纯代码/术语）无需追加
		const prefix = zLine.match(/^#{1,6} /)[0];
		zhLines[z.i] = `${prefix}${z.text}（${e.text}）`;
		changed++;
	}
	fs.writeFileSync(zhPath, zhLines.join("\n"));
	console.log(`OK ${zhRel}: 配对 ${zhList.length} 个标题，追加 ${changed} 个`);
}
process.exit(problems ? 1 : 0);
