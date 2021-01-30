package com.stmgalex.reservation.service.impl;

import static com.stmgalex.reservation.util.NumberUtil.isWithinRange;

import com.stmgalex.reservation.dto.MassDto;
import com.stmgalex.reservation.dto.Statistics;
import com.stmgalex.reservation.entity.Evening;
import com.stmgalex.reservation.entity.EveningReservation;
import com.stmgalex.reservation.entity.Mass;
import com.stmgalex.reservation.entity.MassReservation;
import com.stmgalex.reservation.entity.User;
import com.stmgalex.reservation.exception.EntityNotFoundException;
import com.stmgalex.reservation.exception.ReservationNotFoundException;
import com.stmgalex.reservation.repository.EveningRepository;
import com.stmgalex.reservation.repository.EveningReservationRepository;
import com.stmgalex.reservation.repository.MassRepository;
import com.stmgalex.reservation.repository.MassReservationRepository;
import com.stmgalex.reservation.repository.UserRepository;
import com.stmgalex.reservation.service.AdminService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

  private final MassRepository massRepository;
  private final MassReservationRepository massReservationRepository;
  private final UserRepository userRepository;
  private final EveningRepository eveningRepository;
  private final EveningReservationRepository eveningReservationRepository;

  @Override
  public Statistics getStatistics() {
    List<Mass> masses = massRepository.findAll();
    List<MassReservation> massReservations = massReservationRepository.findAll();
    List<User> users = userRepository.findAll();

    int totalAttendants = masses.stream()
        .filter(mass -> mass.getDate().isBefore(LocalDate.now().plusDays(1)))
        .map(Mass::getReservedSeats)
        .collect(Collectors.summingInt(Integer::intValue));

    int totalAvailable = masses.stream()
        .filter(mass -> mass.getDate().isBefore(LocalDate.now().plusDays(1)))
        .map(Mass::getTotalSeats)
        .collect(Collectors.summingInt(Integer::intValue));

    return Statistics.builder()
        .masses(masses.size())
        .users(users.size())
        .reservations(massReservations.size())
        .approvedReservations(massReservations.stream().filter(MassReservation::isActive).count())
        .canceledReservations(massReservations.stream().filter(r -> !r.isActive()).count())
        .completedMasses(masses.stream().filter(m -> !m.haveSeats()).count())
        .attendancePercentage(totalAttendants * 1.0 / totalAvailable * 100d)
        .leesThan10(getUsersAtAge(users, user -> isWithinRange(1, 10, user.getAge())))
        .lessThan20(getUsersAtAge(users, user -> isWithinRange(11, 20, user.getAge())))
        .lessThan30(getUsersAtAge(users, user -> isWithinRange(21, 30, user.getAge())))
        .lessThan40(getUsersAtAge(users, user -> isWithinRange(31, 40, user.getAge())))
        .lessThan50(getUsersAtAge(users, user -> isWithinRange(41, 50, user.getAge())))
        .moreThan50(getUsersAtAge(users, user -> user.getAge() >= 51))
        .build();
  }


  @Override
  public Page<Mass> getMasses(Pageable pageable, LocalDate date) {
      if (Objects.nonNull(date)) {
          return massRepository.findAllByDateEquals(pageable, date);
      } else {
          return massRepository.findAllByDateGreaterThanEqual(pageable, LocalDate.now());
      }

  }

  @Override
  public Page<Evening> getEvenings(Pageable pageable) {
    return eveningRepository.findAll(pageable);
  }

  @Transactional
  @Override
  public void disableMass(int id) {
    Optional<Mass> optionalMass = massRepository.findById(id);
    Mass mass = optionalMass.orElseThrow(() -> new EntityNotFoundException("عفوا لا توجد قداسات"));
    mass.setEnabled(false);
  }

  @Transactional
  @Override
  public void enableMass(int id) {
    Optional<Mass> optionalMass = massRepository.findById(id);
    Mass mass = optionalMass.orElseThrow(() -> new EntityNotFoundException("عفوا لا توجد قداسات"));
    mass.setEnabled(true);
  }

  @Transactional
  @Override
  public void disableEvening(int id) {
    Optional<Evening> eveningOptional = eveningRepository.findById(id);
    Evening evening = eveningOptional
        .orElseThrow(() -> new EntityNotFoundException("عفوا لا توجد سهرات"));
    evening.setEnabled(false);
  }

  @Transactional
  @Override
  public void enableEvening(int id) {
    Optional<Evening> eveningOptional = eveningRepository.findById(id);
    Evening evening = eveningOptional
        .orElseThrow(() -> new EntityNotFoundException("عفوا لا توجد سهرات"));
    evening.setEnabled(true);
  }

  @Override
  public void exportMassUsers(int id, HttpServletResponse response) throws IOException {
    XSSFWorkbook workbook = new XSSFWorkbook();

    XSSFSheet sheet = workbook.createSheet("Users");

    writeHeaderLine(workbook, sheet);

    writeDataLines(workbook, sheet, id);

    ServletOutputStream outputStream = response.getOutputStream();

    workbook.write(outputStream);

    workbook.close();

    outputStream.close();
  }

  @Transactional
  @Override
  public void updateMass(MassDto massDto) {
    massRepository
        .updateMassById(massDto.getTotalSeats(), massDto.getReservedSeats(), massDto.getId());
  }

  @Override
  public Page<MassReservation> getMassReservations(Pageable pageable) {
    return massReservationRepository.findAll(pageable);
  }

  @Override
  public Page<EveningReservation> getEveningReservations(Pageable pageable) {
    return eveningReservationRepository.findAll(pageable);
  }

  @Transactional
  @Override
  public void disableMassReservation(int id) {
    Optional<MassReservation> optional = massReservationRepository.findById(id);
    MassReservation massReservation = optional
        .orElseThrow(() -> new ReservationNotFoundException("عفوا هذا الحجز غير موجود"));
    massReservation.setActive(false);
  }

  @Transactional
  @Override
  public void enableMassReservation(int id) {
    Optional<MassReservation> optional = massReservationRepository.findById(id);
    MassReservation massReservation = optional
        .orElseThrow(() -> new ReservationNotFoundException("عفوا هذا الحجز غير موجود"));
    massReservation.setActive(true);
  }

  @Transactional
  @Override
  public void disableEveningReservation(int id) {
    Optional<EveningReservation> optional = eveningReservationRepository.findById(id);
    EveningReservation eveningReservation = optional
        .orElseThrow(() -> new ReservationNotFoundException("عفوا هذا الحجز غير موجود"));
    eveningReservation.setActive(false);
  }

  @Transactional
  @Override
  public void enableEveningReservation(int id) {
    Optional<EveningReservation> optional = eveningReservationRepository.findById(id);
    EveningReservation eveningReservation = optional
        .orElseThrow(() -> new ReservationNotFoundException("عفوا هذا الحجز غير موجود"));
    eveningReservation.setActive(true);
  }

  @Override
  public void exportEveningUsers(int id, HttpServletResponse response) throws IOException {

    XSSFWorkbook workbook = new XSSFWorkbook();

    XSSFSheet sheet = workbook.createSheet("Users");

    writeHeaderLine2(workbook, sheet);

    writeDataLines2(workbook, sheet, id);

    ServletOutputStream outputStream = response.getOutputStream();

    workbook.write(outputStream);

    workbook.close();

    outputStream.close();

  }

  @Override
  public Page<MassReservation> getMassReservations(int id, PageRequest pageRequest) {
    return massReservationRepository.findAllByMass_Id(id, pageRequest);
  }

  private void writeDataLines2(XSSFWorkbook workbook, XSSFSheet sheet, int id) {
    int rowCount = 1;

    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setFontHeight(12);
    style.setFont(font);
    style.setWrapText(true);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setAlignment(HorizontalAlignment.CENTER);
    Optional<Evening> optionalEvening = eveningRepository.findById(id);

    Evening evening = optionalEvening
        .orElseThrow(() -> new EntityNotFoundException("عفوا لا توجد سهرات"));

    List<EveningReservation> eveningReservations = evening.getEveningReservations()
        .stream()
        .filter(EveningReservation::isActive)
        .sorted(Comparator.comparing(o -> o.getUser().getName()))
        .collect(Collectors.toList());

    int i = 0;
    for (EveningReservation eveningReservation : eveningReservations) {
      Row row = sheet.createRow(rowCount++);
      int columnCount = 0;
      createCell(sheet, row, columnCount++, i + 1, style);
      createCell(sheet, row, columnCount++, eveningReservation.getUser().getName(), style);
      createCell(sheet, row, columnCount++, eveningReservation.getUser().getNationalId(), style);
      createCell(sheet, row, columnCount++, eveningReservation.getEvening().getDate(), style);
      i++;
    }
  }

  private void writeHeaderLine2(XSSFWorkbook workbook, XSSFSheet sheet) {
    Row row = sheet.createRow(0);

    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setFontHeight(10);
    style.setFont(font);
    style.setWrapText(true);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setAlignment(HorizontalAlignment.CENTER);
    createCell(sheet, row, 0, "رقم الحجز", style);
    createCell(sheet, row, 1, "الاسم", style);
    createCell(sheet, row, 2, "الرقم القومي", style);
    createCell(sheet, row, 3, "اليوم", style);
  }

  private void writeHeaderLine(XSSFWorkbook workbook, XSSFSheet sheet) {

    Row row = sheet.createRow(0);

    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setFontHeight(10);
    style.setFont(font);
    style.setWrapText(true);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setAlignment(HorizontalAlignment.CENTER);
    createCell(sheet, row, 0, "رقم الحجز", style);
    createCell(sheet, row, 1, "الاسم", style);
    createCell(sheet, row, 2, "الرقم القومي", style);
    createCell(sheet, row, 3, "اليوم", style);
    createCell(sheet, row, 4, "الساعة", style);

  }

  private void createCell(XSSFSheet sheet, Row row, int columnCount, Object value,
      CellStyle style) {
    sheet.setColumnWidth(columnCount, 6000);
    Cell cell = row.createCell(columnCount);
    if (value instanceof Integer) {
      sheet.setColumnWidth(columnCount, 2000);
      cell.setCellValue((Integer) value);
    } else if (value instanceof Boolean) {
      cell.setCellValue((Boolean) value);
    } else if (value instanceof LocalDate) {
      sheet.setColumnWidth(columnCount, 3000);
      cell.setCellValue(((LocalDate) value).toString());
    } else if (value instanceof LocalTime) {
      sheet.setColumnWidth(columnCount, 2000);
      cell.setCellValue(((LocalTime) value).toString());
    } else {
      cell.setCellValue((String) value);
    }
    cell.setCellStyle(style);
  }

  private void writeDataLines(XSSFWorkbook workbook, XSSFSheet sheet, int id) {
    int rowCount = 1;

    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setFontHeight(12);
    style.setFont(font);
    style.setWrapText(true);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setAlignment(HorizontalAlignment.CENTER);
    Optional<Mass> optionalMass = massRepository.findById(id);
    Mass mass = optionalMass.orElseThrow(() -> new EntityNotFoundException("عفوا لا توجد قداسات"));
    List<MassReservation> massReservations = mass.getMassReservations()
        .stream()
        .filter(MassReservation::isActive)
        .sorted(Comparator.comparing(o -> o.getUser().getName()))
        .collect(Collectors.toList());

    int i = 0;
    for (MassReservation massReservation : massReservations) {
      Row row = sheet.createRow(rowCount++);
      int columnCount = 0;
      createCell(sheet, row, columnCount++, i + 1, style);
      createCell(sheet, row, columnCount++, massReservation.getUser().getName(), style);
      createCell(sheet, row, columnCount++, massReservation.getUser().getNationalId(), style);
      createCell(sheet, row, columnCount++, massReservation.getMass().getDate(), style);
      createCell(sheet, row, columnCount++, massReservation.getMass().getTime(), style);
      i++;
    }
  }

  private int getUsersAtAge(List<User> users, Predicate<User> predicate) {
    long count = users.stream()
        .filter(predicate)
        .count();
    return (int) count;
  }
}
