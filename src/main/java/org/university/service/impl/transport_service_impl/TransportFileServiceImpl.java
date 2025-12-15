package org.university.service.impl.transport_service_impl;

import org.university.dto.TransportDto;
import org.university.exception.DAOException;
import org.university.service.contract.transport_service.TransportFileService;
import org.university.service.contract.transport_service.TransportGeneralService;

import java.io.*;

public class TransportFileServiceImpl implements TransportFileService {
    private static final String directory_path = "src/main/java/org/university/transport_files";

    private final TransportGeneralService transportGeneralService;

    public TransportFileServiceImpl(TransportGeneralService transportGeneralService) {
        this.transportGeneralService = transportGeneralService;
    }

    @Override
    public void saveTransport(Long transportId) throws DAOException {
        TransportDto transportDto = transportGeneralService.getTransportById(transportId);
        if(transportDto == null) {
            throw new DAOException("Transport with id " + transportId + " does not exist");
        }

        File dir = new File(directory_path);
        if(!dir.exists() || !dir.isDirectory()){
            throw new DAOException("Directory " + directory_path + " does not exist");
        }

        String filePath = directory_path + File.separator + "transport_" + transportDto.getId() + ".ser";

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))){
            oos.writeObject(transportDto);
        }catch (IOException e){
            throw new DAOException("Error writing transport to file: " + filePath);
        }

    }

    @Override
    public TransportDto loadTransportsFromFile(Long transportId) throws DAOException {
        String filePath = directory_path + File.separator + "transport_" + transportId + ".ser";
        File file = new File(filePath);
        if(!file.exists()){
            throw new DAOException("File " + filePath + " does not exist");
        }

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            Object object = ois.readObject();
            if(!(object instanceof TransportDto transportDto)) {
                throw new DAOException("File " + filePath + " does not contain transport data");
            }
            return transportDto;
        }catch(Exception e){
            throw new DAOException("Error reading transport from file: " + filePath);
        }
    }
}
