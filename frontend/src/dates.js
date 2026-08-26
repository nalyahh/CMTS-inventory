export function formatDate(dateString) {
    const [year, month, day] = dateString.split('-')
    const date = new Date(year, month - 1, day)
    const monthName = date.toLocaleDateString('en-US', { month: 'short' })
    return `${monthName} ${date.getDate()} ${date.getFullYear()}`
}