export const CATEGORIES = [
    { value: 'SET', label: 'Set' },
    { value: 'PROPS', label: 'Props' },
    { value: 'SOUND', label: 'Sound' },
    { value: 'LIGHTS', label: 'Lights' },
    { value: 'COSTUMES', label: 'Costumes' },
    { value: 'HAIR_MAKEUP', label: 'Hair and Makeup' },
    { value: 'INSTRUMENTS', label: 'Instruments' },
]

export const CATEGORY_LABELS = Object.fromEntries(
    CATEGORIES.map(category => [category.value, category.label])
)

export const CATEGORY_STYLES = {
    SOUND: { background: '#E1F2FD', color: '#6DB2F3' },
    LIGHTS: { background: '#FDF7E1', color: '#C29A18' },
    PROPS: { background: '#E8FDE1', color: '#4CA845' },
    COSTUMES: { background: '#E7E1FD', color: '#8E6DF3' },
    SET: { background: '#FDE1E1', color: '#F3786D' },
    HAIR_MAKEUP: { background: '#FDE1FF', color: '#B455C0' },
    INSTRUMENTS: { background: '#E1FDF7', color: '#3FB3A9' },
}

export function quantityStyle(available, total) {
    if (available === 0) return { background: '#FDC1C1', color: '#DE5C5C' }
    if (available < total / 2) return { background: '#FDF6C1', color: '#967135' }
    return { background: '#D1FDC1', color: '#359644' }
}
