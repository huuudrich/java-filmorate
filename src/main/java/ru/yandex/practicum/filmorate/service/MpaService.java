package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.properties.MpaRating;

import java.util.List;

@Service
public class MpaService implements MpaDao {
    MpaDao mpaDao;

    public MpaService(@Qualifier("mpaDaoImpl") MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    @Override
    public List<MpaRating> getAllMpa() {
        return mpaDao.getAllMpa();
    }

    @Override
    public MpaRating getMpa(Integer id) throws NotFoundException {
        return mpaDao.getMpa(id);
    }
}
