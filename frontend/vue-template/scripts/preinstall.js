if (!/yarn/.test(process.env.npm_execpath || '')) {
  console.warn(
    `This repository must using yarn as the package manager ` +
      ` for scripts to work properly.`,
  )
  process.exit(1)
}
