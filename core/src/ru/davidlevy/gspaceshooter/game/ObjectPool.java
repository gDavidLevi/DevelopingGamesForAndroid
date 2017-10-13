package ru.davidlevy.gspaceshooter.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Пул объектов
 *
 * @param <T> объекты имплементирующие Poolable
 */
public abstract class ObjectPool<T extends Poolable> {
    /* Список активных обхектов */
    protected List<T> activeList;

    /* Список неактивных обхектов */
    protected List<T> inactiveList;

    /**
     * Конструктор
     */
    public ObjectPool() {
        this.activeList = new ArrayList<T>();
        this.inactiveList = new ArrayList<T>();
    }

    /**
     * Перегруженный конструктор
     *
     * @param poolSize размер пуля
     */
    public ObjectPool(int poolSize) {
        this.activeList = new ArrayList<T>();
        this.inactiveList = new ArrayList<T>();
        for (int i = 0; i < poolSize; i++)
            inactiveList.add(newObject());
    }

    /**
     * Метод создания нового объекта
     *
     * @return T
     */
    protected abstract T newObject();

    /**
     * Дезактивация элемента
     *
     * @param index индекс элемента
     */
    private void deactivationElement(int index) {
        inactiveList.add(activeList.remove(index));
    }

    /**
     * Возвращает активный элемент
     *
     * @return T
     */
    public T getActiveElement() {
        if (inactiveList.size() == 0) inactiveList.add(newObject());
        T element = inactiveList.remove(inactiveList.size() - 1);
        activeList.add(element);
        return element;
    }

    /**
     * Актуализация пула
     */
    public void actualizationPool() {
        /* Пройтись по всем элементам активного списка с конца */
        for (int i = activeList.size() - 1; i >= 0; i--)
            /* Если в этом списке есть неактивные элеенты, то... */
            if (!activeList.get(i).isActive())
                /*... сделать элемент неактивным. */
                deactivationElement(i);
    }

    /**
     * Возвращает активный список
     *
     * @return List
     */
    public List<T> getActiveList() {
        return this.activeList;
    }

    /**
     * Возвращает неактивный список
     *
     * @return List
     */
    public List<T> getInactiveList() {
        return this.inactiveList;
    }
}
