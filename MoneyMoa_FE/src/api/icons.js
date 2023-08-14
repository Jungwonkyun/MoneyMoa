//icon svg 임포트하기
const bankIconModules = import.meta.glob('@/assets/bankIcon/svg/*.svg')
const secuIconModules = import.meta.glob('@/assets/secuIcon/svg/*.svg')

// import nhBankIcon from '@/assets/bankIcon/svg/농협은행.svg'
// console.log(nhBankIcon)
function loadBankIcons() {
  const bankIcons = {}

  for (const path in bankIconModules) {
    bankIconModules[path]().then((mod) => {
      const name = path.split('/').pop().split('.')[0]
      //   console.log(path, mod)
      bankIcons[name] = mod
    })

    // const name = path.split('/').pop().split('.')[0]
    // bankIcons[name] = path.replace(/^\/src/, '@')
    // console.log(path)
  }
  return bankIcons
}
async function loadSecuIcons() {
  const secuIcons = {}
  for (const path in secuIconModules) {
    const name = path.split('/').pop().split('.')[0]
    secuIcons[name] = path.replace(/^\/src/, '@')
  }
  return secuIcons
}
export { loadBankIcons, loadSecuIcons }
